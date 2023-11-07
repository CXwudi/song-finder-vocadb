package mikufan.cx.songfinder.backend.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("artist_names")
data class ArtistName(
  @Id
  val id: Long?,
  val language: NameLanguage,
  @Column("value")
  val name: String,
  val artistId: Long
)