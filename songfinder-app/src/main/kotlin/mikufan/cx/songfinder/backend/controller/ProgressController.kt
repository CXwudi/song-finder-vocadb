package mikufan.cx.songfinder.backend.controller

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import mikufan.cx.songfinder.backend.component.InputFileLineReader
import mikufan.cx.songfinder.backend.component.ProgressTracker
import org.springframework.stereotype.Controller

@Controller
class ProgressController(
  private val progressTracker: ProgressTracker,
  private val inputFileLineReader: InputFileLineReader,
) {

  private var _currentIndex: MutableState<ULong> = mutableStateOf(progressTracker.currentIndex)

  /**
   * Represents the current index state.
   *
   * This variable provides access to the current index state, which is of type [State].
   * It is used for compose component to trigger re-composition when the value in this state is changed.
   *
   * @return the current index state
   */
  val currentIndexState: State<ULong>
    get() = _currentIndex

  val totalCount = progressTracker.totalCount

  val isFinished
    get() = progressTracker.isFinished
}