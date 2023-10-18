package mikufan.cx.songfinder.backend.component

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
  var currentIndex: ULong = 0uL,
) {

  fun increment() {
    currentIndex += 1uL
  }

  val isFinished
    get() = currentIndex >= totalCount
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