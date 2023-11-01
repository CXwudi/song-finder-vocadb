package mikufan.cx.songfinder.backend.service

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import org.springframework.util.ResourceUtils

class InputFileLineReaderTest : ShouldSpec({

  context("line reader") {
    val file = ResourceUtils.getFile("classpath:test-data/dummy-input.txt")
    val inputFileReader = file.bufferedReader()
    val lineReader = InputFileLineReader(
      inputFileReader,
      inputFileReader.lineSequence().filter { it.isNotEmpty() }.iterator()
    )
    should("read lines properly") {
      lineReader.readNext() shouldBe "dummy1"
      lineReader.readNext() shouldBe "dummy2"
      lineReader.readNext() shouldBe "  dummy3"
    }
  }
})
