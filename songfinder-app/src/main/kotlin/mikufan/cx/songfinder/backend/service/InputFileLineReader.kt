package mikufan.cx.songfinder.backend.service

import jakarta.annotation.PreDestroy
import java.io.BufferedReader

/**
 * Handle the file reading of the input file.
 * It tracks the current line and reads next line when [readNext] is called.
 *
 * @property inputFileReader The BufferedReader used for reading lines from the input file.
 * @property itr The iterator over the lines of the input file.
 * @constructor Creates an InputFileLineReader instance with the given parameters.
 */
class InputFileLineReader(
  /**
   * This parameter is only used in [closeReader] method
   */
  private val inputFileReader: BufferedReader,
  private val itr: Iterator<String>,
) {

  fun readNext(): String? {
    while (itr.hasNext()) {
      val line = itr.next()
      if (line.isNotEmpty()) {
        return line
      }
    }
    return null
  }

  @PreDestroy
  fun closeReader() {
    inputFileReader.close()
  }
}