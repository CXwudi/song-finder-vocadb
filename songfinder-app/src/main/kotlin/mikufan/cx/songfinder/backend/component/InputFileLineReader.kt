package mikufan.cx.songfinder.backend.component

import jakarta.annotation.PreDestroy
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.model.IOFiles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.BufferedReader

/**
 * Handle the file reading of the input file.
 * It tracks the current line and read next line when [readNext] is called.
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
      if (line.isNotBlank()) {
        return line.trim()
      }
    }
    return null
  }

  @PreDestroy
  fun closeReader() {
    inputFileReader.close()
  }
}

@Configuration(proxyBeanMethods = false)
class InputFileLineReaderFactory {

  @Bean
  fun inputFileLineReader(ioFile: IOFiles): InputFileLineReader {
    val startLine = ioFile.startLine
    val toSkip: ULong = if (startLine < 1uL) 0uL else startLine - 1uL

    val inputTxt = ioFile.inputTxt
    log.debug { "Opening input file $inputTxt to get ready for user's progress" }
    val inputFileReader = inputTxt.toFile().bufferedReader()
    val itr = inputFileReader.lineSequence().iterator()
    if (toSkip > 0uL) {
      for (i in 0uL until toSkip) {
        itr.next()
      }
    }

    return InputFileLineReader(inputFileReader, itr)
  }
}

private val log = KInlineLogging.logger()