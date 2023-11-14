package mikufan.cx.songfinder.backend.controller.mainpage

import androidx.compose.runtime.State
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.service.SongSearchService
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchResultStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import org.springframework.stereotype.Controller

@Controller
class SearchBarController(
  private val searchInputStateModel: SearchInputStateModel,
  private val searchResultStateModel: SearchResultStateModel,
  private val songSearchService: SongSearchService,
) {

  val currentInputState: State<String> get() = searchInputStateModel.currentInputState
  val currentSearchStatusState: State<SearchStatus> = searchResultStateModel.statusState

  fun setInput(newInput: String) {
    searchInputStateModel.update(newInput)
  }

  suspend fun search() {
    try {
      val title = searchInputStateModel.currentInputState.value
      searchResultStateModel.setAsSearching()
      val results = if (title.isNotEmpty()) {
        songSearchService.search(title)
      } else {
        emptyList()
      }
      searchResultStateModel.setAsDoneWith(results)
    } catch (e: Exception) {
      log.warn(e) { "Exception happened during search, what is that?" }
    }
  }
}

private val log = KInlineLogging.logger()