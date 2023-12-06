package mikufan.cx.songfinder.backend.component.thumbnailfinder

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.core.test.EnabledIf
import io.kotest.matchers.ints.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNot
import io.kotest.matchers.string.beBlank
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.serialization.jackson.*
import io.mockk.every
import io.mockk.mockk
import mikufan.cx.songfinder.backend.db.entity.PvService

class ThumbnailFinderTest : ShouldSpec({

  val client = HttpClient(Java) {
    followRedirects = true
    install(ContentNegotiation) {
      jackson()
    }
  }
  context("NicoNicoDougaThumbnailFinder") {
    val finder = NicoNicoDougaThumbnailFinder(client)
    should("match with NicoNicoDouga") {
      finder.matchedPvService shouldBe PvService.NicoNicoDouga
      finder.match(PvService.NicoNicoDouga) shouldBe true
      finder.match(PvService.Youtube) shouldBe false
    }

    should("find the thumbnail of a PV").config(enabledIf = enablement) {
      val thumbnailInfo = finder.findThumbnail(mockk { every { id } returns "sm42930396" })
      thumbnailInfo.url shouldNot beBlank()
      with(client.get(thumbnailInfo.url).bodyAsChannel()) {
        availableForRead shouldBeGreaterThan 0
      }
    }
  }
})

private val enablement: EnabledIf = { System.getenv("CI") != true.toString() }
