package mikufan.cx.songfinder.backend.db.projection

import mikufan.cx.songfinder.backend.db.entity.ArtistRole
import mikufan.cx.songfinder.backend.db.entity.ArtistType
import mikufan.cx.songfinder.backend.db.entity.NameLanguage
import org.springframework.data.annotation.Transient


class ArtistInSong(
  val id: Long,
  val artistType: ArtistType,
  val defaultNameLanguage: NameLanguage,
  val japaneseName: String,
  val englishName: String,
  val romajiName: String,
  roles: Int,
  val songId: Long,
) {

  @get:Transient
  val roles: List<ArtistRole> = ArtistRole.rolesFromBitString(roles)

  override fun toString(): String =
    "ArtistInSong(id=$id, artistType=$artistType, defaultNameLanguage=$defaultNameLanguage, japaneseName='$japaneseName', englishName='$englishName', romajiName='$romajiName', roles=$roles, songId=$songId)"

  override fun hashCode(): Int = 31 * id.hashCode() + songId.hashCode()

  override fun equals(other: Any?): Boolean {
    if (this === other) return true
    if (other !is ArtistInSong) return false

    if (id != other.id) return false
    if (songId != other.songId) return false

    return true
  }
}
