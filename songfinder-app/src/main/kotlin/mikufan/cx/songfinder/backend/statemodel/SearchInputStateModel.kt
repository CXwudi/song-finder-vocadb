package mikufan.cx.songfinder.backend.statemodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

class SearchInputStateModel(
  initialInput: String
) {
  /**
   * To record the current line of input from file
   */
//  var currentInputFromFile = initialInput

  /**
   * To record the current input content from the search bar
   */
  var currentInputState: MutableState<String> = mutableStateOf(initialInput)

  fun update(newInput: String) {
    currentInputState.value = newInput
  }

//  fun setToNext(newInput: String) {
//    currentInputState.value = newInput
//    currentInputFromFile = newInput
//  }
}