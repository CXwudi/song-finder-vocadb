package mikufan.cx.songfinder.backend.statemodel

import androidx.compose.foundation.lazy.grid.LazyGridState
import kotlinx.coroutines.delay
import org.springframework.stereotype.Component

@Component
class ResultGridStateModel {

  val gridState = LazyGridState()

  suspend fun scrollToTop() {
    // for some reason,
    // when relaxing the filter, the app thinks the first item is visible even though it is not
    // might because it was before the animation happens
//    if (gridState.firstVisibleItemIndex == 0) return
    delay(100)
    gridState.animateScrollToItem(0)
  }
}