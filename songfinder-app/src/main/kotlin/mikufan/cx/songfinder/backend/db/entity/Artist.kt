package mikufan.cx.songfinder.backend.db.entity

import org.springframework.data.annotation.Id
import java.time.LocalDateTime

data class Artist(
  @Id
  val id: Long,
  val artistType: ArtistType,
  val baseVoicebankId: Long?,
  val descriptionOriginal: String,
  val descriptionEnglish: String,
  val mainPictureMime: String?,
  val releaseDate: LocalDateTime?,
  val defaultNameLanguage: NameLanguage,
  val japaneseName: String,
  val englishName: String,
  val romajiName: String
)

