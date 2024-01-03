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
class SoundCloudThumbnailFinder(
  private val client: HttpClient,
) : ThumbnailFinder {
  override val matchedPvService = PvService.SoundCloud

  companion object {
    // https://stackoverflow.com/questions/65352609/how-can-i-get-a-client-id
    private val CLIENT_IDS = listOf(
      "a3e059563d7fd3372b49b37f00a00bcf",
      "RMDyLXBETEYF6qcE4jmndm0VF1QZt2T4"
    )
    private val RANDOM_CLIENT_ID: String
      get() = CLIENT_IDS.random()
  }

  override suspend fun findThumbnail(pv: PVInfo): ThumbnailInfo {
    val id = pv.id.split(" ").first()
    val rsp = withContext(MyDispatchers.ioDispatcher) {
      client.get("https://api-v2.soundcloud.com/tracks/") {
        url {
          appendEncodedPathSegments(id)
          parameters["client_id"] = RANDOM_CLIENT_ID
        }
      }
    }
    val status = rsp.status.value
    when (status) {
      in (200..<300) -> {
        val json = rsp.body<JsonNode>()
        val thumbnailUrlJson = json["artwork_url"]
        if (thumbnailUrlJson.isNull) {
          throw ThumbnailNotAvailableException("The $matchedPvService video $id does not have an artwork")
        } else {
          val thumbnailUrl = thumbnailUrlJson.asText()
          return ThumbnailInfo(thumbnailUrl)
        }
      }

      else -> {
        throw ThumbnailNotAvailableException("The $matchedPvService video $id is not available, status code: $status")
      }
    }
  }
}