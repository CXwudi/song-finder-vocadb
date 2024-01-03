package mikufan.cx.songfinder.backend.component.thumbnailfinder

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.withContext
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import mikufan.cx.songfinder.backend.util.MyDispatchers
import org.springframework.stereotype.Component

@Component
class BilibiliThumbnailFinder(
  private val client: HttpClient,
  private val objectMapper: ObjectMapper
) : ThumbnailFinder {
  override val matchedPvService = PvService.Bilibili

  override suspend fun findThumbnail(pv: PVInfo): ThumbnailInfo {
    val bvid: String? = tryGetBvid(pv.extendedMetadata)
    val rsp: HttpResponse = withContext(MyDispatchers.ioDispatcher) {
      if (bvid == null) {
        client.get("https://api.bilibili.com/x/web-interface/view?aid=${pv.id}")
      } else {
        client.get("https://api.bilibili.com/x/web-interface/view?bvid=$bvid")
      }
    }

    val json = rsp.body<JsonNode>()
    val code = json["code"].asInt()
    if (code != 0) {
      throw ThumbnailNotAvailableException(
        "The $matchedPvService video ${pv.id} is not available, " +
            "code: $code, message: ${json["message"].asText()}"
      )
    } else {
      return ThumbnailInfo(json["data"]["pic"].asText())
    }
  }

  private fun tryGetBvid(extendedMetadata: String?): String? = extendedMetadata?.let { metadataStr ->
    val metadata: JsonNode = objectMapper.readTree(metadataStr)
    metadata["json"]?.get("Bvid")?.asText()
  }
}