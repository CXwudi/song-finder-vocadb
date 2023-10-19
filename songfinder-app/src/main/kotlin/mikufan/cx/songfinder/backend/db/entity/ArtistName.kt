package mikufan.cx.songfinder.backend.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("artist_names")
data class ArtistName(
  @Id
  val id: Long?,
  val language: NameLanguage,
  val value: String,
  val artistId: Long
)