package mikufan.cx.songfinder.backend.db.repository

import io.kotest.matchers.ints.shouldBeGreaterThan
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.util.SpringBootTestWithTestProfile
import mikufan.cx.songfinder.util.SpringShouldSpec
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.util.ResourceUtils

@SpringBootTestWithTestProfile
class PvRepositoryTest(
  private val pvRepository: PvRepository
) : SpringShouldSpec({

  context("PvRepository") {
    should("find all PVs given a list of song IDs") {
      val pvs = pvRepository.findAllBySongIdIn(listOf(205091, 496963)) // 消えゆく想 and Shinagawa
      println("pvs = ${pvs.joinToString("\n", "[\n", "\n]")}")
      pvs.size shouldBeGreaterThan 2
    }
  }
}) {
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
