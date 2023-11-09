package mikufan.cx.songfinder.backend.controller.mainpage

import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.statemodel.SearchResultStatusModel
import org.springframework.stereotype.Controller

@Controller
class ResultPanelController(
  private val resultStatusModel: SearchResultStatusModel
) {
  val currentResultState: List<SongSearchResult> = resultStatusModel.resultState
}