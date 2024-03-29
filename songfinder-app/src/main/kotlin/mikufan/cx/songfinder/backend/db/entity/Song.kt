package mikufan.cx.songfinder.backend.db.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("songs")
data class Song(
  @Id
  val id: Long,
  val lengthSeconds: Int,
  val maxMilliBpm: Int?,
  val minMilliBpm: Int?,
  val nicoId: String?,
  val notesOriginal: String,
  val notesEnglish: String,
  val originalVersionId: Int?,
  val publishDate: LocalDateTime?,
  val releaseEventId: Int?,
  val songType: SongType,
  val defaultNameLanguage: NameLanguage,
  val japaneseName: String,
  val englishName: String,
  val romajiName: String
)

