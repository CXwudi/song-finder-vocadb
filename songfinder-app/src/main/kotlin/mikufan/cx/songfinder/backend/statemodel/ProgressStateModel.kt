package mikufan.cx.songfinder.backend.statemodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class ProgressStateModel(
  startingIndex: ULong,
  val totalCount: ULong,
) {
  val currentIndexState: MutableState<ULong> = mutableStateOf(startingIndex)

  fun increment() {
    currentIndexState.value += 1uL
  }
}