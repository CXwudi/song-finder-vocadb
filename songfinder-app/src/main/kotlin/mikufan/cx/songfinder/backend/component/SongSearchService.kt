package mikufan.cx.songfinder.backend.component

import mikufan.cx.songfinder.backend.db.repository.SongRepository
import org.springframework.stereotype.Service

@Service
class SongSearchService(
  private val songRepo: SongRepository
) {

  fun triggerSearch(title: String) {
    // search step
    // 1. search songs by title

    // 2. find the default name of each song

    // 3. search all PVs

    // 4. search all artists

    // 5. find all default names of each artist
  }
}