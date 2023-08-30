package mikufan.cx.songfinder.backend.controller

import mikufan.cx.songfinder.backend.component.InputFileLineReader
import mikufan.cx.songfinder.backend.component.ProgressTracker
import org.springframework.stereotype.Controller

@Controller
class ProgressController(
  private val progressTracker: ProgressTracker,
  private val inputFileLineReader: InputFileLineReader,
) {

  fun getCurrentCountState() = progressTracker.currentCountState
}