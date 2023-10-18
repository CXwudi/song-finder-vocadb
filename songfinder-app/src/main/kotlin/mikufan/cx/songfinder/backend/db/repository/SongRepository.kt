package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.Song
import org.springframework.stereotype.Repository

@Repository
interface SongRepository : VocaDbCoroutineRepository<Song, Long> {
}