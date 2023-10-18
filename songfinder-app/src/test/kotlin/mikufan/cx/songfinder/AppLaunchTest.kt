package mikufan.cx.songfinder

import io.kotest.matchers.shouldBe
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.util.SpringBootTestWithTestProfile
import mikufan.cx.songfinder.util.SpringShouldSpec
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.util.ResourceUtils

@SpringBootTestWithTestProfile
class AppLaunchTest : SpringShouldSpec({
  context("app") {
    should("boot with dummy IOFiles") {
      1 shouldBe 1
    }
  }
}) {
  /*
   * Unfortunately, impossible to extract this common configuration in common code.
   * So have to copy this in every single spring boot test
   */
  @TestConfiguration
  class DummyConfig {
    @Bean
    fun dummyIOFiles() = IOFiles(
      inputTxt = ResourceUtils.getFile("classpath:test-data/dummy-input.txt").toPath(),
      startLine = 2uL, // 2nd line, which is index 1, which also means skip the index 0 line.
      outputCSV = ResourceUtils.getFile("classpath:test-data/dummy-output.csv").toPath()
    )
  }
}
