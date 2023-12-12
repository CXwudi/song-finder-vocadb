package mikufan.cx.songfinder.backend.controller.mainpage

import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import mikufan.cx.songfinder.backend.service.FindThumbnailService
import org.springframework.stereotype.Controller

@Controller
class ResultCellController(
  private val resultSelectedIntermediateController: ResultSelectedIntermediateController,
  private val findThumbnailService: FindThumbnailService,
) {

  suspend fun handleRecord(chosenSong: SongSearchResult) {
    resultSelectedIntermediateController.writeRecordAndMoveOn(chosenSong.id.toULong())
  }


  suspend fun tryGetThumbnail(pv: PVInfo): Result<ThumbnailInfo> {
    return findThumbnailService.tryGetThumbnail(pv)
  }
}