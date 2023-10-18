package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.SongName
import org.springframework.stereotype.Repository

@Repository
interface SongNameRepository : VocaDbCoroutineRepository<SongName, Long> {
  fun findAllBySongId(id: Long): List<SongName>
}