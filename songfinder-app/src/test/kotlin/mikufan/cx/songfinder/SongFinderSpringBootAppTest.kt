package mikufan.cx.songfinder

import io.kotest.matchers.shouldBe
import mikufan.cx.songfinder.backend.component.InputFileLineReader
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.util.SpringBootTestWithTestProfile
import mikufan.cx.songfinder.util.SpringShouldSpec
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.test.annotation.DirtiesContext
import org.springframework.util.ResourceUtils
import java.nio.file.Paths

@SpringBootTestWithTestProfile
@DirtiesContext
class SongFinderSpringBootAppTest(
  private val inputFileLineReader: InputFileLineReader,
) : SpringShouldSpec({
  context("app") {
    should("start with dummy ioFiles") {
      inputFileLineReader.readNext() shouldBe "dummy2"
      inputFileLineReader.readNext() shouldBe "dummy3"
      inputFileLineReader.readNext() shouldBe null
    }
  }
}) {
  @TestConfiguration
  class DummyConfig {
    @Bean
    fun dummyIOFiles() = IOFiles(
      inputTxt = ResourceUtils.getFile("classpath:test-data/dummy-input.txt").toPath(),
      startLine = 2uL, // 2nd line, which is index 1, which also means skip the index 0 line.
      outputCSV = Paths.get("dummy-output.csv")
    )
  }
}
