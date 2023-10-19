package mikufan.cx.songfinder.backend.model

import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.db.entity.SongType
import java.time.LocalDateTime

data class SongSearchResult(
  val title: String,
  val type: SongType,
  val vocals: List<String>,
  val producers: List<String>,
  val publishDate: LocalDateTime?,
  val pvs: List<PVInfo>
) {

  class Builder {
    private lateinit var title: String
    private lateinit var type: SongType
    private var vocals: MutableList<String> = ArrayList()
    private var producers: MutableList<String> = ArrayList()
    private var publishDate: LocalDateTime? = null
    private var pvs: MutableList<PVInfo> = ArrayList()

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
      return SongSearchResult(title, type, vocals, producers, publishDate, pvs)
    }
  }
}

data class PVInfo(
  val id: String,
  val pvService: PvService
)