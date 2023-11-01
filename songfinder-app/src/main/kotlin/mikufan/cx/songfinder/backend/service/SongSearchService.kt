package mikufan.cx.songfinder.backend.service

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.db.entity.*
import mikufan.cx.songfinder.backend.db.projection.ArtistInSong
import mikufan.cx.songfinder.backend.db.repository.*
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import org.springframework.stereotype.Service

@Service
class SongSearchService(
  private val songRepo: SongRepository,
  private val songNameRepo: SongNameRepository,
  private val pvRepo: PvRepository,
  private val artistRepo: ArtistRepository,
  private val artistNameRepo: ArtistNameRepository,
) {

  suspend fun search(title: String): List<SongSearchResult> = withContext(Dispatchers.IO) {
    log.info { "Searching '$title'" }
    // search step
    // 1. search songs by title
    val songs = songRepo.findByAllPossibleNamesContain(title)
    if (songs.isEmpty()) {
      log.info { "No song found for '$title'" }
      return@withContext emptyList<SongSearchResult>()
    }
    val songIds = songs.map { it.id }
    val songIdToSong: Map<Long, Song> = songs.associateBy { it.id }
    log.debug { "found ${songs.size} entries" }
    val songIdToBuilders: Map<Long, SongSearchResult.Builder> = songs.associate {
      it.id to SongSearchResult.Builder().type(it.songType).publishDate(it.publishDate)
    }
    // 2. find the default name of each song
    val songIdToBuildersThatNeedToCheckUnspecifiedName = fillNamesAndGetThoseThatNeedMoreChecks(songs, songIdToBuilders)

    val songIdToUnspecifiedNames: Map<Long, List<SongName>> = if (songIdToBuildersThatNeedToCheckUnspecifiedName.isNotEmpty()) {
      songNameRepo.findAllBySongIdInAndLanguageIn(
        songIdToBuildersThatNeedToCheckUnspecifiedName.keys.toList(),
        listOf(NameLanguage.Unspecified),
      )
        .groupBy { it.songId }
    } else {
      emptyMap()
    }
    val fillNameTask = launch {
      fillRestOfNames(songIdToBuildersThatNeedToCheckUnspecifiedName, songIdToUnspecifiedNames, songIdToSong)
    }

    log.debug { "filled all song names" }
    // 3. search all PVs
    val songIdToPvs = pvRepo.findAllBySongIdIn(songIds)
      .groupBy { it.songId }
    val fillPvTask = launch { fillPvs(songIdToBuilders, songIdToPvs) }
    log.debug { "filled all PVs" }
    // 4. search all artists
    val artistsBySong: List<ArtistInSong> = artistRepo.findAllBySongIdsIn(songIds)
    // 5. find all default names of each artist
    val artistIdToNames: Map<Long, List<ArtistName>> =
      artistNameRepo.findAllByArtistIdInAndLanguageIn(artistsBySong.mapTo(mutableSetOf()) { it.id }.toList())
        .groupBy { it.artistId }


    log.info { "Found '$title'" }
    fillNameTask.join()
    fillPvTask.join()
    songIdToBuilders.values.map { it.build() }
  }

  private fun fillRestOfNames(
    songIdToBuildersThatNeedCheck: Map<Long, SongSearchResult.Builder>,
    songIdToNames: Map<Long, List<SongName>>,
    songById: Map<Long, Song>
  ) {
    val songIdToUnspecifiedName = songIdToNames.mapValues { (_, names) ->
      names.filter { it.language == NameLanguage.Unspecified }
    }

    for ((songId, builder) in songIdToBuildersThatNeedCheck) {
      val names = songIdToUnspecifiedName[songId]
      if (names.isNullOrEmpty()) {
        log.warn { "No default name found for song $songId, will use Japanese Name as default" }
        builder.title(
          songById[songId]?.japaneseName
            ?: throw IllegalStateException("Should not found no result of song ID $songId here")
        )
      } else {
        builder.title(names.first().name)
      }
    }
  }

  private fun fillNamesAndGetThoseThatNeedMoreChecks(
    songs: List<Song>,
    songIdToBuilder: Map<Long, SongSearchResult.Builder>,
  ): Map<Long, SongSearchResult.Builder> = buildMap {
    for (song in songs) {
      val builder = songIdToBuilder[song.id] ?: throw IllegalStateException(
        "Should not found no result of song ID ${song.id} here"
      )
      when (song.defaultNameLanguage) {
        NameLanguage.Japanese -> builder.title(song.japaneseName)
        NameLanguage.English -> builder.title(song.englishName)
        NameLanguage.Romaji -> builder.title(song.romajiName)
        else -> {
          put(song.id, builder)
        }
      }
    }
  }

  private fun fillPvs(
    resultBuilders: Map<Long, SongSearchResult.Builder>,
    songIdToPvs: Map<Long, List<Pv>>
  ) {
    for ((songId, pvs) in songIdToPvs) {
      val builder = resultBuilders[songId] ?: throw IllegalStateException(
        "Should not found no result of song ID $songId here"
      )
      for (pv in pvs) {
        if (pv.pvService.isOnlineService()) {
          builder.addPV(PVInfo(pv.pvId, pv.pvService, pv.pvType))
        }
      }
    }
  }
}

private val log = KInlineLogging.logger()