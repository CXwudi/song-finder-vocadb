package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.Song
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : VocaDbRepository<Song, Long> {
  @Query("""
    SELECT DISTINCT s.*
    FROM songs s
      JOIN song_names sn ON s.id = sn.song_id
    WHERE
      s.japanese_name REGEXP :title
      OR s.english_name REGEXP :title
      OR s.romaji_name REGEXP :title
      OR sn.value REGEXP :title
  """)
  fun findByAllPossibleNames(title: String): List<Song>
}
