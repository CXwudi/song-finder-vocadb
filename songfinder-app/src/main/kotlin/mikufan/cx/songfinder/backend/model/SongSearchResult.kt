package mikufan.cx.songfinder.backend.model

import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.db.entity.PvType
import mikufan.cx.songfinder.backend.db.entity.SongType
import java.time.LocalDateTime

data class SongSearchResult(
  val id: Long,
  val title: String,
  val type: SongType,
  val vocals: List<String>,
  val producers: List<String>,
  val publishDate: LocalDateTime?,
  val pvs: List<PVInfo>
) {

  class Builder {
    private var id: Long = -1
    private lateinit var title: String
    private lateinit var type: SongType
    private var vocals: MutableList<String> = ArrayList()
    private var producers: MutableList<String> = ArrayList()
    private var publishDate: LocalDateTime? = null
    private var pvs: MutableList<PVInfo> = ArrayList()

    fun id(id: Long) = apply { this.id = id }
    fun title(title: String) = apply { this.title = title }
    fun type(type: SongType) = apply { this.type = type }
    fun addVocal(vocal: String) = apply { this.vocals.add(vocal) }
    fun setVocals(vocals: List<String>) = apply { this.vocals = vocals.toMutableList() }
    fun addProducer(producer: String) = apply { this.producers.add(producer) }
    fun setProducers(producers: List<String>) = apply { this.producers = producers.toMutableList() }
    fun publishDate(publishDate: LocalDateTime?) = apply { this.publishDate = publishDate }
    fun addPV(pv: PVInfo) = apply { this.pvs.add(pv) }
    fun setPvs(pvs: List<PVInfo>) = apply { this.pvs = pvs.toMutableList() }

    fun build(): SongSearchResult {
      return SongSearchResult(id, title, type, vocals, producers, publishDate, pvs)
    }
  }
}

data class PVInfo(
  val id: String,
  val pvService: PvService,
  val pvType: PvType
) {

  val url: String by lazy(LazyThreadSafetyMode.NONE) {
    when (pvService) {
      PvService.NicoNicoDouga -> "https://www.nicovideo.jp/watch/$id"
      PvService.Youtube -> "https://www.youtube.com/watch?v=$id"
      PvService.SoundCloud -> "https://soundcloud.com/${id.split(" ")[1]}"
//      PvService.Vimeo -> "https://vimeo.com/$id"
//      PvService.Piapro -> "https://piapro.jp/t/$id"
      PvService.Bilibili -> "https://www.bilibili.com/video/av$id"
//      PvService.Creofuga -> "https://creofuga.net/$id"
//      PvService.Bandcamp -> "https://bandcamp.com/$id"
      else -> throw IllegalArgumentException("Cannot convert PVInfo to url, pvService is $pvService")
    }
  }
}
