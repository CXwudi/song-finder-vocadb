package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.Pv
import mikufan.cx.songfinder.backend.db.entity.PvService
import org.springframework.stereotype.Repository

@Repository
interface PvRepository : VocaDbRepository<Pv, Long> {
  fun findAllBySongIdInAndPvServiceIn(
    songIds: List<Long>,
    pvServices: List<PvService> = PvService.currentlyRenderableEntries
  ): List<Pv>
}