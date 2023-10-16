package mikufan.cx.songfinder.backend.component

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.model.IOFiles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import kotlin.io.path.bufferedReader

/**
 * Tracks progress based on counts.
 *
 * @property totalCount The total count.
 * @property _currentIndex The mutable state representing the current index.
 * @property currentIndexState The state representing the current index that can trigger re-composition in compose components.
 * @constructor Creates a ProgressTracker instance with the specified total count and optional current index.
 */
class ProgressTracker(
  val totalCount: ULong,
  currentIndex: ULong = 0uL,
) {
  private var _currentIndex:MutableState<ULong> = mutableStateOf(currentIndex)

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


  fun increment() {
    _currentIndex.value += 1uL
  }
}

@Configuration(proxyBeanMethods = false)
class ProgressTrackerFactory {

  @Bean
  fun progressTracker(ioFiles: IOFiles): ProgressTracker {
    val startIndex = maxOf(1uL, ioFiles.startLine) - 1uL
    var count = 0uL
    val inputTxt = ioFiles.inputTxt

    log.debug { "Opening input file $inputTxt to count the total amount of songs" }
    inputTxt.bufferedReader().lineSequence()
      .filter { it.isNotBlank() }
      .forEach { _ -> count++ }
    val realStartIndex = minOf(startIndex, count)
    return ProgressTracker(count, realStartIndex)
  }
}

private val log = KInlineLogging.logger()