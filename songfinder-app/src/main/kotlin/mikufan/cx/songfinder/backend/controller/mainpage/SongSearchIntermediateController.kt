package mikufan.cx.songfinder.backend.controller.mainpage

import kotlinx.coroutines.*
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.service.SongSearchService
import mikufan.cx.songfinder.backend.statemodel.ResultGridStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchOptionsStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchResultStateModel
import org.springframework.stereotype.Controller

/**
 * Intermediate controller for song search functionality.
 * This class handles triggering the search and updating the search state.
 * It also handles the cancellation of the previous search job if a new search is triggered.
 * @property searchInputStateModel The state model for search input.
 * @property searchOptionsStateModel The state model for search options.
 * @property searchResultStateModel The state model for search results.
 * @property songSearchService The service used for song search.
 */
@Controller
class SongSearchIntermediateController(
  private val searchInputStateModel: SearchInputStateModel,
  private val searchOptionsStateModel: SearchOptionsStateModel,
  private val searchResultStateModel: SearchResultStateModel,
  private val resultGridStateModel: ResultGridStateModel,
  private val songSearchService: SongSearchService,
) {

  /**
   * The current search job.
   */
  var searchJob: Job? = null

  /**
   * Triggers a search operation with an optional delay. Cancels the previous search job if it exists.
   *
   * @param wait The delay in milliseconds before starting the search. Defaults to 500ms.
   */
  suspend fun triggerSearch(wait: Long = 500) = coroutineScope {
    searchJob?.cancel()
    searchJob = launch {
      try {
        delay(wait) // do a small delay waiting for any rapid user input
        doSearch()
      } catch (e: CancellationException) {
        log.info { "Previous job is cancelled" }
      } catch (e: Exception) {
        log.warn(e) { "Exception happened during search, what is that?" }
      }
    }
  }

  /**
   * Performs a search using the current input state and search options.
   * Sets the search result state model accordingly.
   *
   * @throws Exception if an error occurs during the search process.
   */
  private suspend fun doSearch() {
    searchResultStateModel.setAsSearching()
    val title = searchInputStateModel.currentInputState.value
    val regexOption = searchOptionsStateModel.searchRegexOptionState.value
    val results = if (title.isNotEmpty()) {
      songSearchService.search(title, regexOption)
    } else {
      emptyList()
    }
    searchResultStateModel.setAsDoneWith(results)
    if (results.isNotEmpty()) {
      resultGridStateModel.scrollToTop()
    }
  }
}

private val log = KInlineLogging.logger()