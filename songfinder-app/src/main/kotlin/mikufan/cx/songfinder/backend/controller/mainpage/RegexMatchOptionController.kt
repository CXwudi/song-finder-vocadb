package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.runtime.State
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchOptionsStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchRegexOption
import org.springframework.stereotype.Controller

@Controller
class RegexMatchOptionController(
  private val searchOptionsStateModel: SearchOptionsStateModel,
  searchInputStateModel: SearchInputStateModel,
  private val songSearchIntermediateController: SongSearchIntermediateController
) {

  val currentRegexOptionState: State<SearchRegexOption> = searchOptionsStateModel.searchRegexOptionState
  val currentInputState: State<String> = searchInputStateModel.currentInputState

  suspend fun setRegexOption(newOption: SearchRegexOption) {
    searchOptionsStateModel.searchRegexOptionState.value = newOption
    songSearchIntermediateController.triggerSearch(100)
  }
}