package mikufan.cx.songfinder.ui.component.main

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.backend.controller.mainpage.ResultCellController
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.db.entity.SongType
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import mikufan.cx.songfinder.backend.util.MyDispatchers
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.spacing
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * Composable function that represents a grid cell in a lazy grid.
 *
 * @param result The song search result to display in the grid cell.
 * @param scopeFromIrremovableParent The coroutine scope from the parent that cannot be removed.
 * @param controller The result cell controller to handle interactions with the grid cell.
 */
@Composable
fun LazyGridItemScope.ResultGridCell(
  result: SongSearchResult,
  scopeFromIrremovableParent: CoroutineScope,
  controller: ResultCellController = getSpringBean(),
) {
  val callbacks = ResultCellCallbacks(
    onCardClicked = { scopeFromIrremovableParent.launch { controller.handleRecord(it) } },
    provideThumbnailInfo = controller::tryGetThumbnail
  )
  RealResultGridCell(result, callbacks)
}

/**
 * The real composable for displaying a real music search result.
 *
 * @param result The search result for the music.
 * @param callbacks The callbacks for interacting with the result.
 * @param modifier The modifier for styling the grid cell.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridItemScope.RealResultGridCell(
  result: SongSearchResult,
  callbacks: ResultCellCallbacks,
  modifier: Modifier = Modifier
) {
  val pvs = result.pvs
  MusicCardTemplate(
    onCardClicked = { callbacks.onCardClicked(result) },
    modifier = modifier.animateItem()
  ) {
    LazilyFetchedThumbnail(
      pvs,
      provideThumbnailInfoCallback = callbacks.provideThumbnailInfo,
    )
    MusicInfo(result, pvs)
  }
}

/**
 * Lazily fetch and display thumbnail, using the first ever successful thumbnail URL from the
 * given list of PVs.
 *
 * @param pvs The list of PVInfo objects representing the thumbnail images to display.
 * @param imageHolderModifier The modifier for styling the image holder.
 * @param provideThumbnailInfoCallback The callback function for providing thumbnail information.
 */
@Composable
fun LazilyFetchedThumbnail(
  pvs: List<PVInfo>,
  imageHolderModifier: Modifier = Modifier
    .size(120.dp)
    .clip(RoundedCornerShape(MaterialTheme.spacing.cornerShape)),
  provideThumbnailInfoCallback: suspend (PVInfo) -> Result<ThumbnailInfo>,
) {
  // if no PVs, display a "no image" icon
  if (pvs.isEmpty()) {
    IconFromResources(
      iconResourcePath = "image/image-not-found-icon.svg",
      contentDescription = "No Thumbnail"
    )
  } else {
    // else, starting from index 0
    val currentPvInfoIndexStatue = remember { mutableStateOf(0) }
    var currentPvInfoIndex by currentPvInfoIndexStatue

    var loadStatus: ThumbnailInfoLoadStatus by remember { mutableStateOf(ThumbnailInfoLoadStatus.Loading) }
    LaunchedEffect(currentPvInfoIndex) {
      loadStatus = ThumbnailInfoLoadStatus.Loading
      provideThumbnailInfoCallback(pvs[currentPvInfoIndex]).fold(
        // if success, continue, else, try next
        onSuccess = { loadStatus = ThumbnailInfoLoadStatus.Success(it) },
        onFailure = {
          if (currentPvInfoIndex < pvs.size - 1) {
            currentPvInfoIndex++
          } else {
            loadStatus = ThumbnailInfoLoadStatus.Failure
          }
        }
      )
    }

    Crossfade(loadStatus, animationSpec = tween()) {
      when (it) {
        is ThumbnailInfoLoadStatus.Loading -> ThumbnailBox {
          CircularProgressIndicator()
        }
        // we reach here if all PVs failed to load the thumbnail
        // render an "image failed" icon
        is ThumbnailInfoLoadStatus.Failure -> IconFromResources(
          iconResourcePath = "image/image-load-failed.svg",
          contentDescription = "Failed Thumbnail"
        )

        is ThumbnailInfoLoadStatus.Success -> RealThumbnail(
          load = it,
          currentPvInfoIndexStatue = currentPvInfoIndexStatue,
          pvSize = pvs.size,
          imageHolderModifier = imageHolderModifier,
          onCurrentPvInfoIndexChange = { newVal -> currentPvInfoIndex = newVal },
          onLoadStatusChanged = { newVal -> loadStatus = newVal },
        )
      }
    }

  }
}

@Composable
fun IconFromResources(
  iconResourcePath: String,
  contentDescription: String,
) = ThumbnailBox {
  Image(
    painter = painterResource(iconResourcePath),
    contentDescription = contentDescription,
    modifier = Modifier.size(72.dp),
  )
}

@Composable
fun RealThumbnail(
  load: ThumbnailInfoLoadStatus.Success,
  currentPvInfoIndexStatue: MutableState<Int>,
  pvSize: Int,
  imageHolderModifier: Modifier = Modifier
    .size(120.dp)
    .clip(RoundedCornerShape(MaterialTheme.spacing.cornerShape)),
  onCurrentPvInfoIndexChange: (Int) -> Unit,
  onLoadStatusChanged: (ThumbnailInfoLoadStatus) -> Unit,
) {
  val (url, requestBuilder) = load.info

  // using KamelImage, if the thumbnail URL works, it will be rendered
  // else, move on the next PV
  val urlHandler = LocalUriHandler.current
  val currentPvInfoIndex = currentPvInfoIndexStatue.value
  KamelImage(
    resource = {
      asyncPainterResource(url) {
        coroutineContext += MyDispatchers.ioDispatcher

        requestBuilder {
          requestBuilder.invoke(this)
        }
      }
    },
    contentDescription = "Thumbnail",
    modifier = imageHolderModifier
      .clickable { urlHandler.openUri(url) },
    onLoading = {
      CircularProgressIndicator(
        progress = { it },
      )
    },
    onFailure = {
      if (currentPvInfoIndex < pvSize - 1) {
        onCurrentPvInfoIndexChange(currentPvInfoIndex + 1)
      } else {
        log.warn(it) { "Kamel failed to load thumbnail for $url" }
        onLoadStatusChanged(ThumbnailInfoLoadStatus.Failure)
      }
    },
    animationSpec = tween()
  )
}

/**
 * Displays music information for a given song.
 *
 * @param songInfo the information of the song being displayed, including its id, title, type, vocals, producers, and publish date.
 * @param filteredPvs a list of PVInfo objects representing the PVs (promotional videos) related to the song.
 */
@Composable
fun MusicInfo(songInfo: SongSearchResult, filteredPvs: List<PVInfo>) = Column {
  val (id, title, type, vocals, producers, publishDate, _) = songInfo
  SongTitle(id, title)
  ArtistField(vocals, producers)
  PublishDateField(publishDate)
  SongTypeField(type)
  PvField(filteredPvs)
}

/**
 * Displays the title of a song.
 *
 * @param id The ID of the song.
 * @param title The title of the song.
 */
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

/**
 * Displays the artist string based on the given vocals and producers lists.
 *
 * @param vocals A list of vocal artists.
 * @param producers A list of producer artists.
 */
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

/**
 * Displays the publish date of the song.
 *
 * @param publishDate The publish date of the song.
 * @optIn Requires [ExperimentalFoundationApi] to be enabled.
 */
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

/**
 * Displays the type of song.
 *
 * @param type The type of the song.
 */
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

/**
 * Displays a list of PVs.
 *
 * @param pvs The list of PVInfo objects representing the PVs to be displayed.
 *
 * @see PVInfo
 */
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

/**
 * Represents a set of callback functions used by a result cell in a search interface.
 *
 * @property onCardClicked A callback function that is invoked when the result cell is clicked.
 *                         It takes a [SongSearchResult] as a parameter and does not return any value.
 * @property provideThumbnailInfo A callback function that asynchronously provides a thumbnail URL for the result cell.
 *                               It takes a [SongSearchResult] as a parameter and returns a [String] representing the URL.
 */
data class ResultCellCallbacks(
  val onCardClicked: (SongSearchResult) -> Unit,
  val provideThumbnailInfo: suspend (PVInfo) -> Result<ThumbnailInfo>,
)

private const val UnknownArtist = "Unknown Artist"

/**
 * Returns a formatted string representing an artist based on the given vocals and producers lists.
 *
 * @param vocals The list of vocals for the artist.
 * @param producers The list of producers for the artist.
 * @return A formatted string representing the artist.
 *         If both vocals and producers lists are empty, returns "UnknownArtist".
 *         If vocals list is empty, returns the formatted string of producers.
 *         If producers list is empty, returns the formatted string of vocals.
 *         Otherwise, returns the formatted string of producers followed by "feat."
 *         and the formatted string of vocals.
 */
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

/**
 * A Composable function that creates a music card template.
 *
 * @param onCardClicked A callback function to be executed when the card is clicked.
 * @param modifier Optional modifier for the card.
 * @param content The content of the card.
 */
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


@Composable
fun ThumbnailBox(
  modifier: Modifier = Modifier,
  content: @Composable () -> Unit
) = Box(
  contentAlignment = Alignment.Center,
  modifier = modifier
    .size(120.dp)
    .clip(RoundedCornerShape(MaterialTheme.spacing.cornerShape)),
) {
  content()
}


sealed interface ThumbnailInfoLoadStatus {
  data object Loading : ThumbnailInfoLoadStatus
  data class Success(
    val info: ThumbnailInfo
  ) : ThumbnailInfoLoadStatus

  data object Failure : ThumbnailInfoLoadStatus
}

private val log = KInlineLogging.logger()