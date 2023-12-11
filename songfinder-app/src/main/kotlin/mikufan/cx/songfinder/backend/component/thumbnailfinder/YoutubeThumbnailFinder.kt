package mikufan.cx.songfinder.backend.component.thumbnailfinder

import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import org.springframework.stereotype.Component

@Component
class YoutubeThumbnailFinder : ThumbnailFinder {
  override val matchedPvService = PvService.Youtube

  override suspend fun findThumbnail(pv: PVInfo): ThumbnailInfo {
    val id = pv.id
    // https://orbitingweb.com/blog/view-youtube-thumbnail-image/
    // It mentioned that not all videos have maxresdefault.jpg, so we should use hqdefault.jpg for better compatibility
    return ThumbnailInfo("https://img.youtube.com/vi/$id/hqdefault.jpg")
  }
}