package mikufan.cx.songfinder.backend.db.repository

import io.kotest.matchers.shouldBe
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.util.SpringBootTestWithTestProfile
import mikufan.cx.songfinder.util.SpringShouldSpec
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.util.ResourceUtils

@SpringBootTestWithTestProfile
class ArtistRepositoryTest(
  private val artistRepository: ArtistRepository
) : SpringShouldSpec({
  context("ArtistRepository") {
    should("find all artists by ID") {
      // 4 songs: 消えゆく想い, Shinagawa, Lucky*Orb, and 流星ハンター from 40mP
      val artists = artistRepository.findAllBySongIdsIn(listOf(205091, 496963, 236970, 30138))
      println("artists = ${artists.joinToString("\n", "[\n", "\n]")} ")
      artists.size shouldBe 18
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
