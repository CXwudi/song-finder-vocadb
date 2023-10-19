package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.Song
import mikufan.cx.songfinder.backend.db.entity.SongWithNames
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : VocaDbRepository<Song, Long> {
  @Query("""
    SELECT DISTINCT 
    # same as SongWithOneName
      s.id, s.original_version_id, s.song_type, s.publish_date, 
      s.default_name_language, s.japanese_name, s.english_name, s.romaji_name, 
      sn.language AS song_name_language, sn.value AS song_name
    FROM songs s
      JOIN song_names sn ON s.id = sn.song_id
    WHERE
      s.japanese_name LIKE concat('%', :title, '%')
      OR s.english_name LIKE concat('%', :title, '%')
      OR s.romaji_name LIKE concat('%', :title, '%')
      OR sn.value LIKE concat('%', :title, '%')
  """)
  fun findAllWithAllPossibleNames(title: String): List<SongWithNames>
}
