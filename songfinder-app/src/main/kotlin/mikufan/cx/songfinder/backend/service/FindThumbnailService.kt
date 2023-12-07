package mikufan.cx.songfinder.backend.service

import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.withContext
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.component.thumbnailfinder.ThumbnailException
import mikufan.cx.songfinder.backend.component.thumbnailfinder.ThumbnailFinder
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import org.springframework.cache.CacheManager
import org.springframework.cache.get
import org.springframework.stereotype.Service

@Service
class FindThumbnailService(
  thumbnailFinders: List<ThumbnailFinder>,
  private val cacheManager: CacheManager,
) {

  private val finderMap = thumbnailFinders.associateBy { it.matchedPvService }

  companion object {
    const val CACHE_NAME = "thumbnail"
  }

  //  @Cacheable("thumbnail", key = "#pv.id + #pv.pvService")
  suspend fun tryGetThumbnail(pv: PVInfo): Result<ThumbnailInfo> = withContext(ThumbnailFinder.defaultDispatcher) {
    val cachedThumbnailInfoResult = cacheManager[CACHE_NAME]?.get<Result<ThumbnailInfo>>(pv.id + pv.pvService)
    if (cachedThumbnailInfoResult != null) {
      log.debug { "Found cached thumbnail info $cachedThumbnailInfoResult for PV $pv" }
      cachedThumbnailInfoResult
    } else {
      doGetAndSaveCacheConditionally(pv)
    }
  }

  private suspend fun doGetAndSaveCacheConditionally(pv: PVInfo): Result<ThumbnailInfo> {
    val finder = finderMap[pv.pvService]
    return if (finder == null) {
      val r = Result.failure<ThumbnailInfo>(IllegalArgumentException("No thumbnail finder for pv service ${pv.pvService}"))
      cachePut(pv, r)
      r
    } else {
      try {
        log.info { "First time searching thumbnail for PV $pv, try finding" }
        val thumbnailInfo = finder.findThumbnail(pv)
        log.info { "Successfully found thumbnail info $thumbnailInfo for PV $pv" }
        val r = Result.success(thumbnailInfo)
        cachePut(pv, r)
        r
      } catch (e: ThumbnailException) {
        log.warn { "Failed to find thumbnail info for PV $pv, exception: ${e.message}" }
        val r = Result.failure<ThumbnailInfo>(e)
        cachePut(pv, r)
        r
      } catch (e: CancellationException) {
        log.info {
          "Cancellation happens upon finding thumbnail info for PV $pv, " +
              "likely this happens when we are scrolling too fast. avoiding caching and returning. $e"
        }
        Result.failure(e)
      } catch (e: Exception) {
        log.warn(e) {
          "Failed to find thumbnail info for PV $pv due to unexpected exception, " +
              "avoiding caching and returning"
        }
        Result.failure(e)
      }
    }
  }

  private fun cachePut(pv: PVInfo, thumbnailInfoResult: Result<ThumbnailInfo>) {
    cacheManager[CACHE_NAME]?.put(pv.id + pv.pvService, thumbnailInfoResult)
  }

  //  @CacheEvict("thumbnail", allEntries = true)
  fun evictCache() {
    log.info { "Evicting all thumbnail cache" }
    cacheManager[CACHE_NAME]?.clear()
  }
}

private val log = KInlineLogging.logger()