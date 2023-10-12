package mikufan.cx.songfinder.backend.controller

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

  fun readNextLine() = inputFileLineReader.readNext()

  fun writeSongIdAndIncrementIndex(songId: ULong) {
    outputCsvLineWriter.writeSongId(songId)
    progressTracker.increment()
  }
}