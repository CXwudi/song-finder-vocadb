package mikufan.cx.songfinder.backend.controller

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import mikufan.cx.songfinder.backend.component.InputFileLineReader
import mikufan.cx.songfinder.backend.component.OutputCsvLineWriter
import mikufan.cx.songfinder.backend.component.ProgressTracker
import org.springframework.stereotype.Controller

@Controller
class IOController(
  private val inputFileLineReader: InputFileLineReader,
  private val outputCsvLineWriter: OutputCsvLineWriter,
  private val progressTracker: ProgressTracker,
) {

  private val _currentLineState by lazy { mutableStateOf(inputFileLineReader.readNext() ?: "") }

  val currentLineState: State<String> get() = _currentLineState

  fun readNextLineToState() {
    _currentLineState.value = inputFileLineReader.readNext() ?: throw IllegalStateException("No more lines to read")
  }

  fun writeSongIdAndIncrementIndex(songId: ULong) {
    outputCsvLineWriter.writeSongId(songId)
    progressTracker.increment()
  }
}