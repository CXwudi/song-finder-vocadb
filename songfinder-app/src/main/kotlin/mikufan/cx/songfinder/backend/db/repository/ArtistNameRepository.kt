package mikufan.cx.songfinder.backend.db.repository

import mikufan.cx.songfinder.backend.db.entity.ArtistName
import mikufan.cx.songfinder.backend.db.entity.NameLanguage
import org.springframework.stereotype.Repository

@Repository
interface ArtistNameRepository : VocaDbRepository<ArtistName, Long> {
  fun findAllByArtistIdInAndLanguageIn(artistIds: List<Long>, languages: List<NameLanguage> = NameLanguage.entries): List<ArtistName>
}