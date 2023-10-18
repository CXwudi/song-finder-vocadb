package mikufan.cx.songfinder.backend.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("song_names")
data class SongName(
  @Id
  val id: Long,
  val language: NameLanguage,
  @Column("value")
  val name: String,
  val songId: Long
)
