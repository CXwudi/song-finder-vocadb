package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.Pv
import org.springframework.stereotype.Repository

@Repository
interface PvRepository : VocaDbRepository<Pv, Long> {
  fun findAllBySongIdIn(songIds: List<Long>): List<Pv>
}