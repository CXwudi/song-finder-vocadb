package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.runtime.State
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchResultStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import org.springframework.stereotype.Controller

@Controller
class SearchBarController(
  private val searchInputStateModel: SearchInputStateModel,
  searchResultStateModel: SearchResultStateModel,
  private val songSearchIntermediateController: SongSearchIntermediateController
) {

  val currentInputState: State<String> get() = searchInputStateModel.currentInputState
  val currentSearchStatusState: State<SearchStatus> = searchResultStateModel.statusState

  fun setInput(newInput: String) {
    searchInputStateModel.update(newInput)
  }

  suspend fun search() {
    songSearchIntermediateController.triggerSearch()
  }
}