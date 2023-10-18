package mikufan.cx.songfinder.backend.model

import java.time.LocalDateTime

data class SongSearchResult(
  val title: String,
  val vocals: List<String>,
  val producers: List<String>,
  val publishDate: LocalDateTime,
  val pvs: List<PVInfo>
)

data class PVInfo(
  val id: String,
  //TODO: the PVService enum to be created
)
