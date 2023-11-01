package mikufan.cx.songfinder.backend.service

import com.fasterxml.jackson.dataformat.csv.CsvMapper
import com.fasterxml.jackson.dataformat.csv.CsvParser
import com.fasterxml.jackson.dataformat.csv.CsvSchema
import jakarta.annotation.PreDestroy
import mikufan.cx.songfinder.backend.model.IOFiles
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.io.BufferedWriter
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import kotlin.io.path.bufferedWriter
import kotlin.io.path.notExists

class OutputCsvLineWriter(
  private var nextCount: ULong,
  private val csvWriter: BufferedWriter,
) {

  fun writeSongId(vocadbId: ULong) {
    val line = "${nextCount++},$vocadbId,"
    csvWriter.write(line)
    csvWriter.newLine()
    csvWriter.flush()
  }

  @PreDestroy
  fun close() {
    csvWriter.close()
  }
}

@Configuration(proxyBeanMethods = false)
class OutputCsvLineWriterFactory {

  @Bean
  fun outputCsvLineWriter(ioFiles: IOFiles): OutputCsvLineWriter {
    val outputFile = ioFiles.outputCSV
    val nextCount = determineStartingNextCount(outputFile)
    val fileWriter = outputFile.bufferedWriter(options = arrayOf(StandardOpenOption.APPEND))
    return OutputCsvLineWriter(nextCount, fileWriter)
  }

  private fun determineStartingNextCount(outputFile: Path): ULong = if (outputFile.notExists()) {
    1uL
  } else {
    var count = 0uL
    // map first column only as order
    val csvColumnSchema = CsvSchema.builder()
      .addColumn("order")
      .build()
    CsvMapper().readerForMapOf(String::class.java).with(csvColumnSchema)
      .withFeatures(
        CsvParser.Feature.ALLOW_COMMENTS,
        CsvParser.Feature.SKIP_EMPTY_LINES,
        CsvParser.Feature.IGNORE_TRAILING_UNMAPPABLE,
        CsvParser.Feature.TRIM_SPACES
      )
      .readValues<Map<String, String>>(outputFile.toFile())
      .forEach {
        count = maxOf(count, requireNotNull(it["order"]?.toULong()) { "Make sure the first column exists"})
      }
    count + 1uL
  }
}
