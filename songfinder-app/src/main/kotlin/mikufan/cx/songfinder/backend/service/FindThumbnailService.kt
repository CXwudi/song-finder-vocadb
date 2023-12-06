package mikufan.cx.songfinder.backend.service

import mikufan.cx.songfinder.backend.component.thumbnailfinder.ThumbnailFinder
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import org.springframework.stereotype.Service

@Service
class FindThumbnailService(
  thumbnailFinders: List<ThumbnailFinder>,
) {

  private val finderMap = thumbnailFinders.associateBy { it.matchedPvService }

  suspend fun tryGetThumbnail(pv: PVInfo): Result<ThumbnailInfo> {
    val finder = finderMap[pv.pvService]
    return if (finder == null) {
      Result.failure(IllegalArgumentException("No thumbnail finder for pv service ${pv.pvService}"))
    } else {
      try {
        Result.success(finder.findThumbnail(pv))
      } catch (e: Exception) {
        Result.failure(e)
      }
    }
  }

  fun evictCache() {
    TODO("return back here once spring cache is added")
  }
}