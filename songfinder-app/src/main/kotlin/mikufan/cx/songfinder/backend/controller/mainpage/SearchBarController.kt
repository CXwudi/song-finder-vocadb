package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.runtime.State
import mikufan.cx.songfinder.backend.service.SongSearchService
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import org.springframework.stereotype.Controller

@Controller
class SearchBarController(
  private val searchInputStateModel: SearchInputStateModel,
  private val songSearchService: SongSearchService,
) {

  val currentInputState: State<String> get() = searchInputStateModel.currentInputState

  fun setInput(newInput: String) {
    searchInputStateModel.update(newInput)
  }

  suspend fun search() {
    songSearchService.search(searchInputStateModel.currentInputState.value)
  }
}