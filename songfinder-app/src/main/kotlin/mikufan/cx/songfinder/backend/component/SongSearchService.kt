package mikufan.cx.songfinder.backend.component

import mikufan.cx.songfinder.backend.db.repository.SongRepository
import org.springframework.stereotype.Service

@Service
class SongSearchService(
  private val songRepo: SongRepository
) {

  fun triggerSearch(title: String) {

  }
}