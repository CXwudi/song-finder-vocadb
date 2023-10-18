package mikufan.cx.songfinder.backend.controller

import mikufan.cx.songfinder.backend.component.InputFileLineReader
import mikufan.cx.songfinder.backend.component.ProgressTracker
import org.springframework.stereotype.Controller

@Controller
class ProgressController(
  private val progressTracker: ProgressTracker,
  private val inputFileLineReader: InputFileLineReader,
) {

  val currentCountState
    get() = progressTracker.currentIndexState

  val totalCount = progressTracker.totalCount

  val isFinished
    get() = progressTracker.currentIndexState.value >= progressTracker.totalCount
}