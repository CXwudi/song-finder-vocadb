package mikufan.cx.songfinder.backend.component

import io.kotest.assertions.fail
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import mikufan.cx.songfinder.backend.model.IOFiles
import org.springframework.util.ResourceUtils
import java.nio.file.Files
import kotlin.io.path.deleteIfExists
import kotlin.io.path.readLines
import kotlin.io.path.writeText


class OutputCsvLineWriterTest : ShouldSpec({
  context("factory") {
    val factory = OutputCsvLineWriterFactory()
    val tempOutputFile = Files.createTempFile("dummy-output", ".csv")
    val originalOutputFile = ResourceUtils.getFile("classpath:test-data/dummy-output.csv")
    tempOutputFile.writeText(originalOutputFile.readText())

    should("properly create csv writer") {
      val csvLineWriter = factory.outputCsvLineWriter(IOFiles(mockk(), 1uL, tempOutputFile))
      csvLineWriter.writeSongId(1234uL)
      tempOutputFile.readLines().lastOrNull()?.let {
        it.split(',')[0] shouldBe "4"
      } ?: fail("should not be empty")
      tempOutputFile.deleteIfExists() shouldBe true
    }
  }
})
