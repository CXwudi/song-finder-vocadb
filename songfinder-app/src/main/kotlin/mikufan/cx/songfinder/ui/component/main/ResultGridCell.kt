package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import compose.icons.FontAwesomeIcons
import compose.icons.SimpleIcons
import compose.icons.fontawesomeicons.Solid
import compose.icons.fontawesomeicons.solid.Question
import compose.icons.simpleicons.Bilibili
import compose.icons.simpleicons.Niconico
import compose.icons.simpleicons.Soundcloud
import compose.icons.simpleicons.Youtube
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mikufan.cx.songfinder.backend.controller.mainpage.ResultCellController
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.db.entity.SongType
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.spacing
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun LazyGridItemScope.ResultGridCell(
  result: SongSearchResult,
  scopeFromIrremovableParent: CoroutineScope,
  controller: ResultCellController = getSpringBean(),
) {
  val callbacks = ResultCellCallbacks(
    onCardClicked = { scopeFromIrremovableParent.launch { controller.handleRecord(it) } },
    provideThumbnailUrl = { TODO() }
  )
  RealResultGridCell(result, callbacks)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridItemScope.RealResultGridCell(
  result: SongSearchResult,
  callbacks: ResultCellCallbacks,
  modifier: Modifier = Modifier
) {
  val filteredPvs: List<PVInfo> by produceState(listOf()) {
    value = result.pvs.filter {
      it.pvService in listOf(
        PvService.Youtube,
        PvService.NicoNicoDouga,
        PvService.SoundCloud,
        PvService.Bilibili
      )
    }
  }
  MusicCardTemplate(
    onCardClicked = { callbacks.onCardClicked(result) },
    modifier.animateItemPlacement()
  ) {
    LazyThumbnailImage(result, filteredPvs)
    MusicInfo(result, filteredPvs)
  }
}

@Composable
fun LazyThumbnailImage(
  result: SongSearchResult,
  pvs: List<PVInfo>
) {
  //TODO: use the first ever available PV's thumbnail, if no PVs, use image not found.
  // If exceptions (typically no available PVs), use image failed to load

  // Loading process: starting from the first PV, if failed to load, try the next one. If all failed, use image not found

  Image(
    painter = painterResource("image/image-not-found-icon.svg"),
    contentDescription = "Thumbnail",
    modifier = Modifier
      .size(120.dp)
      .clip(RoundedCornerShape(MaterialTheme.spacing.cornerShape))
  )
}

@Composable
fun MusicInfo(songInfo: SongSearchResult, filteredPvs: List<PVInfo>) = Column {
  val (id, title, type, vocals, producers, publishDate, _) = songInfo
  SongTitle(id, title)
  ArtistField(vocals, producers)
  PublishDateField(publishDate)
  SongTypeField(type)
  PvField(filteredPvs)
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongTitle(id: Long, title: String) {
  Row(
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing, Alignment.CenterHorizontally),
    modifier = Modifier.fillMaxWidth()
  ) {
    val uriHandler = LocalUriHandler.current
    TooltipAreaWithCard(
      tip = {
        Text(
          """
            Click to check more song info at
            https://vocadb.net/S/$id
          """.trimIndent()
        )
      },
    ) {
      Text(
        text = title,
        style = MaterialTheme.typography.titleLarge,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.basicMarquee().clickable {
          uriHandler.openUri("https://vocadb.net/S/$id")
        },
      )
    }
  }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun ArtistField(vocals: List<String>, producers: List<String>) {
  val artistString by produceState("Loading Artists...") {
    value = getArtistString(vocals, producers)
  }
  Text(
    text = artistString,
    style = MaterialTheme.typography.titleMedium,
    maxLines = 1,
    overflow = TextOverflow.Ellipsis,
    modifier = Modifier.basicMarquee(),
  )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PublishDateField(publishDate: LocalDateTime?) {
  val publishDateText by produceState("Loading...") {
    value = publishDate?.format(DateTimeFormatter.ISO_DATE) ?: "Unknown"
  }
  Text(
    text = "Publish Date: $publishDateText",
    style = MaterialTheme.typography.bodyMedium,
    maxLines = 1,
    modifier = Modifier.basicMarquee(),
  )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SongTypeField(type: SongType) {
  Text(
    text = "Song Type: $type",
    style = MaterialTheme.typography.bodyMedium,
    maxLines = 1,
    modifier = Modifier.basicMarquee(),
  )
}

@Composable
private fun PvField(pvs: List<PVInfo>) {
  val uriHandler = LocalUriHandler.current
  LazyRow(
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    item {
      Text(
        text = if (pvs.isEmpty()) "No PVs" else "PVs:",
        style = MaterialTheme.typography.bodyMedium,
      )
    }
    if (pvs.isNotEmpty()) {
      items(pvs) { pv ->
        TooltipAreaWithCard(
          tip = {
            Text(
              """
                Click to check the PV at
                ${pv.url}
              """.trimIndent()
            )
          },
        ) {
          Icon(
            imageVector = when (pv.pvService) {
              PvService.Youtube -> SimpleIcons.Youtube
              PvService.NicoNicoDouga -> SimpleIcons.Niconico
              PvService.SoundCloud -> SimpleIcons.Soundcloud
              PvService.Bilibili -> SimpleIcons.Bilibili
              else -> FontAwesomeIcons.Solid.Question
            },
            contentDescription = "${pv.pvService} Icon",
            modifier = Modifier
              .size(24.dp)
              .clickable {
                uriHandler.openUri(pv.url)
              }
          )
        }
      }
    }
  }
}

/* --------- Utils --------- */

data class ResultCellCallbacks(
  val onCardClicked: (SongSearchResult) -> Unit,
  val provideThumbnailUrl: suspend (SongSearchResult) -> String,
)

private val UnknownArtist = "Unknown Artist"

internal fun getArtistString(vocals: List<String>, producers: List<String>): String {
  val vocalString = vocals.joinToString(", ")
  val producerString = producers.joinToString(", ")
  return when {
    vocalString.isEmpty() && producerString.isEmpty() -> UnknownArtist
    vocalString.isEmpty() -> producerString
    producerString.isEmpty() -> vocalString
    else -> "$producerString feat. $vocalString"
  }
}

@Composable
internal fun MusicCardTemplate(
  onCardClicked: () -> Unit,
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) {
  Card(
    shape = RoundedCornerShape(MaterialTheme.spacing.cornerShapeLarge),
    modifier = Modifier.then(modifier).clickable { onCardClicked() }
  ) {
    Row(
      verticalAlignment = Alignment.CenterVertically,
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingLarge),
      modifier = Modifier.padding(MaterialTheme.spacing.paddingLarge)
    ) {
      content()
    }
  }
}