package mikufan.cx.songfinder.backend.config

import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.backend.service.InputFileLineReader
import mikufan.cx.songfinder.backend.statemodel.ProgressStateModel
import mikufan.cx.songfinder.backend.statemodel.SearchInputStateModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.file.Path
import kotlin.io.path.bufferedReader

@Configuration(proxyBeanMethods = false)
class InputRelatedConfig(
  ioFiles: IOFiles,
) {
  private final val startIndex: ULong
  private final val inputTxt: Path

  init {
    startIndex = maxOf(1uL, ioFiles.startLine) - 1uL
    inputTxt = ioFiles.inputTxt
  }

  @Bean
  fun progressModel(): ProgressStateModel {
    var count = 0uL
    log.debug { "Opening input file $inputTxt to count the total amount of songs" }
    inputTxt.bufferedReader().use { reader ->
      reader.lineSequence()
        .filter { it.isNotEmpty() }
        .forEach { _ -> count++ }
    }
    val realStartIndex = minOf(startIndex, count)
    return ProgressStateModel(realStartIndex, count)
  }

  @Bean
  fun inputFileLineReader(): InputFileLineReader {
    log.debug { "Opening input file $inputTxt to get ready for user's progress" }
    val inputFileReader = inputTxt.toFile().bufferedReader()
    val itr = inputFileReader.lineSequence()
      .filter { it.isNotEmpty() }
      .iterator()
    if (startIndex > 0uL) {
      for (i in 0uL until startIndex) {
        if (itr.hasNext()) {
          itr.next()
        }
      }
    }

    return InputFileLineReader(inputFileReader, itr)
  }

  @Bean
  fun searchInputStateModel(lineReader: InputFileLineReader): SearchInputStateModel {
    val s = lineReader.readNext() ?: ""
    return SearchInputStateModel(s)
  }
}

private val log = KInlineLogging.logger()