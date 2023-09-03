package mikufan.cx.songfinder.backend.component

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.mockk
import mikufan.cx.songfinder.backend.model.IOFiles
import org.springframework.util.ResourceUtils


class InputFileLineReaderTest : ShouldSpec({
  context("factory") {
    val factory = InputFileLineReaderFactory()
    should("properly create line reader") {
      factory.inputFileLineReader(
        IOFiles(
          inputTxt = ResourceUtils.getFile("classpath:test-data/dummy-input.txt").toPath(),
          startLine = 2uL, // 2nd line, which is index 1, which also means skip the index 0 line.
          outputCSV = mockk()
        )
      ).apply {
        readNext() shouldBe "dummy2"
        readNext() shouldBe "dummy3"
        readNext() shouldBe null
      }
    }
  }
})
