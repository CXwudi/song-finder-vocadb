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
 * @property _currentCount The mutable state representing the current count.
 * @property currentCountState The state representing the current count that can trigger re-composition in compose components.
 * @constructor Creates a ProgressTracker instance with the specified total count and optional current count.
 */
class ProgressTracker(
  val totalCount: ULong,
  currentCount: ULong = 1uL,
) {
  private var _currentCount:MutableState<ULong> = mutableStateOf(currentCount)

  /**
   * Represents the current count state.
   *
   * This variable provides access to the current count state, which is of type [State].
   * It is used for compose component to trigger re-composition when the value in this state is changed.
   *
   * @return the current count state
   */
  val currentCountState: State<ULong>
    get() = _currentCount


  fun increment() {
    _currentCount.value += 1uL
  }
}

@Configuration(proxyBeanMethods = false)
class ProgressTrackerFactory {

  @Bean
  fun progressTracker(ioFiles: IOFiles): ProgressTracker {
    val startLine = maxOf(1uL, ioFiles.startLine)
    var count = 0uL
    val inputTxt = ioFiles.inputTxt

    log.debug { "Opening input file $inputTxt to count the total amount of songs" }
    inputTxt.bufferedReader().lineSequence()
      .filter { it.isNotBlank() }
      .forEach { _ -> count++ }
    return ProgressTracker(count, startLine)
  }
}

private val log = KInlineLogging.logger()