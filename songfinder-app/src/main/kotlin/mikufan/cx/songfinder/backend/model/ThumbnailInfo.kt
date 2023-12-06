package mikufan.cx.songfinder.backend.model

import io.ktor.client.request.*

data class ThumbnailInfo(
  val url: String,
  val requestBuilder: HttpRequestBuilder.() -> Unit
)
