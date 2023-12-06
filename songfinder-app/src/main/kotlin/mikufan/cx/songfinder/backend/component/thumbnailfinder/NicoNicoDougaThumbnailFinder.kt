package mikufan.cx.songfinder.backend.component.thumbnailfinder

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import org.springframework.stereotype.Component
import kotlin.random.Random

@Component
class NicoNicoDougaThumbnailFinder(
  private val client: HttpClient
) : ThumbnailFinder {
  override val matchedPvService: PvService = PvService.NicoNicoDouga

  companion object {
    private const val INFO_API = "https://www.nicovideo.jp/api/watch/v3_guest/%s"
    private val defaultHeaders = mapOf(
      "x-Frontend-Id" to "6",
      "x-Frontend-version" to "0",
      HttpHeaders.Accept to "*/*"
    )

  }

  override suspend fun findThumbnail(pv: PVInfo): ThumbnailInfo {
    val id = pv.id
    val url = INFO_API.format(id)

    val infoJson = client.get(url) {
      headers {
        defaultHeaders.forEach { (k, v) -> append(k, v) }
      }
      parameter("actionTrackId", generateRandomTrackId())
    }.body<JsonNode>()
    val thumbnailUrl = infoJson["data"]["video"]["thumbnail"]["ogp"].asText()

    return ThumbnailInfo(thumbnailUrl) {
      headers {
        defaultHeaders.forEach { (k, v) -> append(k, v) }
      }
    }
  }

  private fun generateRandomTrackId(): String {
    val random = Random(System.currentTimeMillis())
    val intChars = ('0'..'9')
    val stringChars = ('A'..'Z') + ('a'..'z') + intChars
    val stringPart = (0..<10).map { stringChars.random(random) }.joinToString("")
    val intPart = (0..<13).map { intChars.random(random) }.joinToString("")
    return "${stringPart}_${intPart}"
  }

}