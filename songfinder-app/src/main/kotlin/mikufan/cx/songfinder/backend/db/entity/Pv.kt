package mikufan.cx.songfinder.backend.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("pvs_for_songs")
data class Pv(

  @Id
  val id: Long,
  val author: String,
  val name: String,
  val pvId: String,
  val pvType: PvType,
  @Column("pv_service")
  val pvService: PvService,
  val extendedMetadata: String?,
  val publishDate: LocalDateTime?,
  val songId: Long
)

enum class PvType {
  Original,
  Reprint,
  Other
}

enum class PvService {
  NicoNicoDouga,
  Youtube,
  SoundCloud,
  Vimeo,
  Piapro,
  Bilibili,
  File,
  LocalFile,
  Creofuga,
  Bandcamp
}