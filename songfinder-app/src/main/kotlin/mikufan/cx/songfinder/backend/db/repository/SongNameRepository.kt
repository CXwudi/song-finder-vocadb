package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.NameLanguage
import mikufan.cx.songfinder.backend.db.entity.SongName
import org.springframework.stereotype.Repository

@Repository
interface SongNameRepository : VocaDbRepository<SongName, Long> {
  fun findAllBySongIdInAndLanguageIn(ids: List<Long>, languages: List<NameLanguage> = NameLanguage.entries): List<SongName>
}