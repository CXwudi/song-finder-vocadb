package mikufan.cx.songfinder.backend.statemodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class SearchInputStateModel(
  initialInput: String
) {

  var currentInputState: MutableState<String> = mutableStateOf(initialInput)

  fun update(newInput: String) {
    currentInputState.value = newInput
  }
}