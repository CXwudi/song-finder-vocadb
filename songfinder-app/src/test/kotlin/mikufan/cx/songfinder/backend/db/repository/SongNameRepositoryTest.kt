package mikufan.cx.songfinder.backend.db.repository

import io.kotest.matchers.shouldBe
import mikufan.cx.songfinder.backend.db.entity.NameLanguage
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.util.SpringBootTestWithTestProfile
import mikufan.cx.songfinder.util.SpringShouldSpec
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.util.ResourceUtils


@SpringBootTestWithTestProfile
class SongNameRepositoryTest(
  private val songNameRepository: SongNameRepository,
) : SpringShouldSpec({
  context("SongNameRepository") {
    should("find all names by ID") {
      val names = songNameRepository.findAllBySongId(205091)
      println("names = $names")
      names.size shouldBe 3
      names.first { it.language == NameLanguage.Romaji }.name shouldBe "Kie Yuku Omoi"
      names.first { it.language == NameLanguage.Japanese}.name shouldBe "消えゆく想い"
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
