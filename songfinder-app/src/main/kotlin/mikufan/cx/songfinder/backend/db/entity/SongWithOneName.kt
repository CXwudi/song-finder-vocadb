package mikufan.cx.songfinder.backend.db.entity

import java.time.LocalDateTime

data class SongWithNames(
  val id: Long,
  val originalVersionId: Long?,
  val publishDate: LocalDateTime?,
  val songType: SongType,
  val defaultNameLanguage: NameLanguage,
  val japaneseName: String,
  val englishName: String,
  val romajiName: String,
  val songNameLanguage: NameLanguage,
  val songName: String?
)
