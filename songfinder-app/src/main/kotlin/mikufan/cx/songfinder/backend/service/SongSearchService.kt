package mikufan.cx.songfinder.backend.service

import kotlinx.coroutines.delay
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.db.repository.SongRepository
import mikufan.cx.songfinder.backend.model.SongSearchResult
import org.springframework.stereotype.Service

@Service
class SongSearchService(
  private val songRepo: SongRepository
) {

  suspend fun search(title: String): SongSearchResult? {
    log.info { "Searching '$title'" }
    // search step
    // 1. search songs by title

    // 2. find the default name of each song

    // 3. search all PVs

    // 4. search all artists

    // 5. find all default names of each artist
    delay(1500)
    log.info { "Found '$title'" }
    return null
  }
}

private val log = KInlineLogging.logger()