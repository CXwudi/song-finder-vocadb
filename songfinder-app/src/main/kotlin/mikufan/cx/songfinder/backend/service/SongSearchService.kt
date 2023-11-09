package mikufan.cx.songfinder.backend.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.db.entity.*
import mikufan.cx.songfinder.backend.db.projection.ArtistInSong
import mikufan.cx.songfinder.backend.db.repository.*
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import org.springframework.stereotype.Service

/**
 * Typealias representing a unique identifier for a song.
 */
private typealias SongId = Long
/**
 * Typealias representing the ID of an artist.
 */
private typealias ArtistId = Long

/**
 * Service class that provides functionality to search for songs.
 *
 * @property songRepo The repository for songs.
 * @property songNameRepo The repository for song names.
 * @property pvRepo The repository for PVs.
 * @property artistRepo The repository for artists.
 * @property artistNameRepo The repository for artist names.
 */
@Service
class SongSearchService(
  private val songRepo: SongRepository,
  private val songNameRepo: SongNameRepository,
  private val pvRepo: PvRepository,
  private val artistRepo: ArtistRepository,
  private val artistNameRepo: ArtistNameRepository,
) {

  companion object {
    const val UNKNOWN = "Unknown"
    val ioDispatcher = Dispatchers.IO
    val defaultDispatcher = Dispatchers.Default
  }

  /**
   * Searches for songs based on the given title.
   *
   * @param title The title to search for.
   * @return A list of SongSearchResult objects matching the search criteria.
   */
  suspend fun search(title: String): List<SongSearchResult> {
    log.info { "Searching '$title'" }
    // search steps:
    // 1. search songs by title
    val songs = withContext(ioDispatcher) {
      songRepo.findByAllPossibleNamesContain(title)
    }
    log.debug { "found ${songs.size} entries" }
    if (songs.isEmpty()) {
      log.info { "No song found for '$title'" }
      return emptyList()
    }
    val songIds = songs.map { it.id }

    // 2. find the name of each song with unspecified name
    val songIdsOfUnspecifiedName = songs
      .filter { it.defaultNameLanguage == NameLanguage.Unspecified }
      .map { it.id }
    log.debug { "${songIdsOfUnspecifiedName.size} songs has unspecified names, requires additional checking" }
    val songIdOfUnspecifiedNameToNames: Map<SongId, List<SongName>> =
      withContext(ioDispatcher) {
        songNameRepo.findAllBySongIdInAndLanguageIn(songIdsOfUnspecifiedName, listOf(NameLanguage.Unspecified))
      }
        .groupBy { it.songId }

    // 3. search all PVs
    val songIdToPv: Map<SongId, List<Pv>> = withContext(ioDispatcher) {
      pvRepo.findAllBySongIdIn(songIds)
    }
      .groupBy { it.songId }
    log.debug { "found ${songIdToPv} PVs" }

    // 4. search all artists
    val artistsInSongs = withContext(ioDispatcher) {
      artistRepo.findAllBySongIdsIn(songIds)
    }
    val songIdToArtistsInSongs: Map<SongId, List<ArtistInSong>> = artistsInSongs.groupBy { it.songId }
    log.debug { "found ${artistsInSongs.size} artists" }

    // 5. find the name of each artist with unspecified name
    val artistIdsOfUnspecifiedName = artistsInSongs
      .filter { it.defaultNameLanguage == NameLanguage.Unspecified }
      .map { it.id }
    val artistIdWithUnspecifiedNameToNames: Map<ArtistId, List<ArtistName>> =
      withContext(ioDispatcher) {
        artistNameRepo.findAllByArtistIdInAndLanguageIn(artistIdsOfUnspecifiedName, listOf(NameLanguage.Unspecified))
      }
        .groupBy { it.artistId }

    // 6. concurrently assemble the result, with order preserving
    val results = withContext(defaultDispatcher) {
      songs.map { song ->
        val titleTask = async { findDefaultLanguageTitle(song, songIdOfUnspecifiedNameToNames) }
        val pvsTask =  async { fillPvs(song.id, songIdToPv) }
        val vocalsTask = async { fillVocals(song.id, songIdToArtistsInSongs, artistIdWithUnspecifiedNameToNames) }
        val producerTask = async { fillProducers(song.id, songIdToArtistsInSongs, artistIdWithUnspecifiedNameToNames) }
        async {
          SongSearchResult(
            id = song.id,
            title = titleTask.await(),
            type = song.songType,
            publishDate = song.publishDate,
            pvs = pvsTask.await(),
            vocals = vocalsTask.await(),
            producers = producerTask.await()
          )
        }
      }.map { it.await() }
    }

    log.info { "Found ${results.size} results for '$title'" }
    return results
  }


  /**
   * Finds the default language title for a given song.
   *
   * @param song The song for which to find the default title.
   * @param songIdOfUnspecifiedNameToNames A map containing the song ID as key and a list of song names as value.
   * Only those with default name to unspecified names are included.
   * @return The default language title for the song.
   */
  private fun findDefaultLanguageTitle(
    song: Song,
    songIdOfUnspecifiedNameToNames: Map<SongId, List<SongName>>
  ): String = when (song.defaultNameLanguage) {
    NameLanguage.Japanese -> song.japaneseName
    NameLanguage.English -> song.englishName
    NameLanguage.Romaji -> song.romajiName
    NameLanguage.Unspecified -> {
      val names = songIdOfUnspecifiedNameToNames[song.id]
      if (names.isNullOrEmpty()) {
        log.warn { "No default name found for song ${song.id}, will use Japanese Name '${song.japaneseName}' as default" }
        song.japaneseName
      } else {
        names.first().name
      }
    }
  }

  /**
   * Fills the PVs for a given song ID using a map of song IDs to PVs.
   *
   * @param songId The ID of the song for which to retrieve the PVs.
   * @param songIdToPvs A map of song IDs to lists of PVs.
   * @return A list of PVInfo objects representing the PVs for the given song ID if found; otherwise, an empty list.
   */
  private fun fillPvs(
    songId: SongId,
    songIdToPvs: Map<SongId, List<Pv>>
  ): List<PVInfo> = songIdToPvs[songId]?.let { pvs ->
    pvs.filter { it.pvService.isOnlineService() }
      .map { PVInfo(it.pvId, it.pvService, it.pvType) }
  } ?: emptyList<PVInfo>().also {
    log.warn { "No PV found for song $songId" }
  }

  /**
   * Extracts the vocals (virtual singers) for a given song ID.
   *
   * @param id The ID of the song.
   * @param songIdToArtistsInSongs A map that contains song IDs as keys and corresponding artist lists as values.
   * @param artistIdWithUnspecifiedNameToNames A map that contains artist IDs with unspecified names as keys and corresponding artist names as values.
   * @return A list of strings representing the vocals for the given song ID.
   * @throws IllegalStateException if no result is found for the given song ID.
   */
  private fun fillVocals(
    id: SongId,
    songIdToArtistsInSongs: Map<SongId, List<ArtistInSong>>,
    artistIdWithUnspecifiedNameToNames: Map<ArtistId, List<ArtistName>>
  ): List<String> = fillBy(id, songIdToArtistsInSongs, artistIdWithUnspecifiedNameToNames) {
    it.artistType.isVirtualSinger() || it.artistType in setOf(ArtistType.Vocalist, ArtistType.OtherVocalist)
        || it.roles.any { role -> role in setOf(ArtistRole.Vocalist) }
  }


  /**
   * Fills the list of producers for a given ID.
   *
   * @param id The ID of the entity to fill the producers for.
   * @param songIdToArtistsInSongs A map of song IDs to lists of artists in songs.
   * @param artistIdWithUnspecifiedNameToNames A map of artist IDs with unspecified names to lists of artist names.
   * @return A list of producers for the given ID.
   */
  private fun fillProducers(
    id: Long,
    songIdToArtistsInSongs: Map<SongId, List<ArtistInSong>>,
    artistIdWithUnspecifiedNameToNames: Map<ArtistId, List<ArtistName>>
  ): List<String> = fillBy(id, songIdToArtistsInSongs, artistIdWithUnspecifiedNameToNames) {
    it.artistType.isSongProducer() || it.artistType in setOf(ArtistType.OtherGroup, ArtistType.OtherIndividual)
        || it.roles.any { role -> role.isProducerRole() }
  }

  /**
   * Fills a list of artist names based on the given parameters.
   *
   * @param id The ID of the song for which to fill the artist names.
   * @param songIdToArtistsInSongs A map that contains the mapping of song IDs to a list of artists for each song.
   * @param artistIdWithUnspecifiedNameToNames A map that contains the mapping of artist IDs with unspecified names to
   * a list of names for each artist.
   * @param filter A lambda function used to filter the artist list.
   * @return A list of filled artist names based on the given parameters.
   *
   * @throws IllegalStateException if no artists are found for the specified song ID.
   */
  private fun fillBy(
    id: Long,
    songIdToArtistsInSongs: Map<SongId, List<ArtistInSong>>,
    artistIdWithUnspecifiedNameToNames: Map<ArtistId, List<ArtistName>>,
    filter: (ArtistInSong) -> Boolean
  ): List<String> {
    val artistsInSong = songIdToArtistsInSongs[id]
      ?: throw IllegalStateException("Very unlikely to found no artists of song ID $id here")
    return artistsInSong
      .filter(filter)
      .map { artistInSong ->
        val name = when (artistInSong.defaultNameLanguage) {
          NameLanguage.Japanese -> artistInSong.japaneseName
          NameLanguage.English -> artistInSong.englishName
          NameLanguage.Romaji -> artistInSong.romajiName
          NameLanguage.Unspecified -> {
            val names = artistIdWithUnspecifiedNameToNames[artistInSong.id]
            if (names.isNullOrEmpty()) {
              log.warn { "No default name found for artist ${artistInSong.id}, will use Japanese Name '${artistInSong.japaneseName}' as default" }
              artistInSong.japaneseName
            } else {
              names.first().name
            }
          }
        }
        removeUnknown(name)
      }
  }

  /**
   * Removes the occurrences of the word 'Unknown', 'unknown' and '()' from the given artist string.
   *
   * @param artistStr the artist string to remove the occurrences from
   * @return the modified artist string with the occurrences removed
   */
  private fun removeUnknown(artistStr: String): String {
    return if (artistStr.lowercase().contains("${UNKNOWN.lowercase()} producer")) {
      artistStr
    } else {
      artistStr
        .replace(UNKNOWN.lowercase(), "")
        .replace(UNKNOWN.uppercase(), "")
        .replace(UNKNOWN, "")
        .replace(" ()", "")
        .replace("()", "")
    }
  }
}

private val log = KInlineLogging.logger()