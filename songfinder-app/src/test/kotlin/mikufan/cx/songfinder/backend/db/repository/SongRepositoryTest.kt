package mikufan.cx.songfinder.backend.db.repository

import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.util.SpringBootTestWithTestProfile
import mikufan.cx.songfinder.util.SpringShouldSpec
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.util.ResourceUtils

@SpringBootTestWithTestProfile
class SongRepositoryTest(
  private val songRepository: SongRepository
) : SpringShouldSpec({

  context("SongRepository") {
    should("be able to count") {
      val count = songRepository.count()
      count shouldBeGreaterThan 460_000
      println("count = $count")
    }

    should("get info of one song") {
      val lukaSong = songRepository.findById(205091).get()
      println("lukaSong = $lukaSong")
      lukaSong.japaneseName shouldBe "消えゆく想い"
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
