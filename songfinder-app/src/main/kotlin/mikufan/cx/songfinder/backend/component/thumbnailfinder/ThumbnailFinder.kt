package mikufan.cx.songfinder.backend.component.thumbnailfinder

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.asCoroutineDispatcher
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import java.util.concurrent.Executors

interface ThumbnailFinder {
  /**
   * Represents the PvService that a ThumbnailFinder can match with.
   * [PvService] includes various online services such as [PvService.NicoNicoDouga], [PvService.Youtube], [PvService.SoundCloud], etc.
   *
   * @see ThumbnailFinder
   */
  val matchedPvService: PvService

  /**
   * Check if this [ThumbnailFinder] can match with the given [pvService]
   *
   * @param pvService the [PvService] to check
   * @return true if this [ThumbnailFinder] can match with the given [pvService]
   *
   * @see ThumbnailFinder
   */
  fun match(pvService: PvService): Boolean = pvService == matchedPvService

  /**
   * Finds the thumbnail for the given [PVInfo].
   *
   * @param pv the [PVInfo] containing information about the PV
   * @return the [ThumbnailInfo] containing the URL and request builder for the thumbnail
   */
  suspend fun findThumbnail(pv: PVInfo): ThumbnailInfo

  companion object {
    val ioDispatcher = Executors.newVirtualThreadPerTaskExecutor().asCoroutineDispatcher()
    val defaultDispatcher = Dispatchers.Default
  }
}
