package mikufan.cx.songfinder.backend.component.thumbnailfinder

import com.fasterxml.jackson.databind.JsonNode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.withContext
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import mikufan.cx.songfinder.backend.util.MyDispatchers
import org.springframework.stereotype.Component

@Component
class NicoNicoDougaThumbnailFinder(
  private val client: HttpClient
) : ThumbnailFinder {
  override val matchedPvService: PvService = PvService.NicoNicoDouga

  companion object {
    // thanks for the unofficial API from https://niconicolibs.github.io/api/other/index.html
    private const val INFO_API = "https://nvapi.nicovideo.jp/v1/videos?watchIds=%s"
    private val defaultHeaders = mapOf(
      "x-Frontend-Id" to "6",
      "x-Frontend-version" to "0",
      HttpHeaders.Accept to "*/*"
    )
  }

  override suspend fun findThumbnail(pv: PVInfo): ThumbnailInfo {
    val id = pv.id
    val url = INFO_API.format(id)

    val response = withContext(MyDispatchers.ioDispatcher) {
      client.get(url) {
        headers {
          defaultHeaders.forEach { (k, v) -> append(k, v) }
        }
      }
    }
    val infoJson = response.body<JsonNode>()
    return when (response.status.value) {
      in (400..<500) -> {
        val status = infoJson["meta"]["status"].asText()
        val errorCode = infoJson["meta"]["errorCode"].asText()
        throw ThumbnailNotAvailableException(
          "The $matchedPvService video $id is not available, " +
              "status: $errorCode, error code: $status"
        )
      }
      in (200..<300) -> {
        val thumbnailUrl = infoJson["data"]["items"][0]["video"]["thumbnail"]["nHdUrl"].asText()
        ThumbnailInfo(thumbnailUrl)
      }
      else -> error("Failed to get thumbnail for PV $pv, response status: ${response.status}")
    }

  }
}