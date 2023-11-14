package mikufan.cx.songfinder.backend.statemodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import mikufan.cx.songfinder.backend.model.SongSearchResult
import org.springframework.stereotype.Component

@Component
class SearchResultStateModel {
  val statusState: MutableState<SearchStatus> = mutableStateOf(SearchStatus.Searching)
  val resultState: SnapshotStateList<SongSearchResult> = mutableStateListOf()

  fun setAsSearching() {
    statusState.value = SearchStatus.Searching
  }

  fun setAsDoneWith(results: List<SongSearchResult>) {
    statusState.value = SearchStatus.Done
    resultState.clear()
    resultState.addAll(results)
  }
}

enum class SearchStatus {
  Searching, Done
}