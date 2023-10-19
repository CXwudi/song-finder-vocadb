package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.Artist
import mikufan.cx.songfinder.backend.db.projection.ArtistInSong
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.stereotype.Repository

@Repository
interface ArtistRepository : VocaDbRepository<Artist, Long> {
  @Query("""
    SELECT a.id, a.artist_type, 
    a.default_name_language, a.japanese_name, a.english_name, a.romaji_name, 
    afs.roles, afs.song_id
    FROM artists a 
      JOIN artists_for_songs afs 
    WHERE a.id = afs.artist_id 
    AND afs.song_id IN (:songIds)
  """)
  fun findAllBySongIdsIn(songIds: List<Long>): List<ArtistInSong>
}