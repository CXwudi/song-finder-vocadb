package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.foundation.lazy.grid.LazyGridState
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.statemodel.ResultGridStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchResultStateModel
import org.springframework.stereotype.Controller

@Controller
class ResultPanelController(
  resultStatusModel: SearchResultStateModel,
  gridStateModel: ResultGridStateModel,
) {
  val currentResultState: List<SongSearchResult> = resultStatusModel.resultState
  val resultGridState: LazyGridState = gridStateModel.gridState
}