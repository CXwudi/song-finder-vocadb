@file:OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)

package mikufan.cx.songfinder.ui.test

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import compose.icons.SimpleIcons
import compose.icons.simpleicons.Niconico
import compose.icons.simpleicons.Youtube
import kotlinx.coroutines.launch
import mikufan.cx.songfinder.ui.common.ColumnCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import mikufan.cx.songfinder.ui.theme.spacing

fun main() = application {
  Window(onCloseRequest = ::exitApplication) {
    MyAppThemeWithSurface {
      ColumnCentralizedWithSpacing {
        Text("Hello World")
        LazyVerticalGrid(
          columns = GridCells.Adaptive(300.dp),
          horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
          verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
          modifier = Modifier.padding(horizontal = MaterialTheme.spacing.padding),
        ) {
          item {
            MusicCard(onCardClicked = {
              println("clicked")
            })
          }
          item {
            MusicCard(onCardClicked = {
              println("clicked")
            })
          }
        }
      }

    }
  }
}

@Composable
fun MusicCardTemplate(onCardClicked: suspend () -> Unit, content: @Composable () -> Unit) {
  val scope = rememberCoroutineScope()
  Card(
    shape = RoundedCornerShape(MaterialTheme.spacing.cornerShapeLarge),
    modifier = Modifier.clickable {
      scope.launch {
        onCardClicked()
      }
    }
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
fun MusicCard(onCardClicked: suspend () -> Unit) {
  MusicCardTemplate(onCardClicked) {
    ThumbnailImage()
    Column {
      Row(
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing, Alignment.CenterHorizontally),
        modifier = Modifier.fillMaxWidth()
      ) {
        val uriHandler = LocalUriHandler.current
        TooltipAreaWithCard(
          tip = {
            Text("Click to open the song page")
          },
        ) {
          Text(
            text = "Song Name",
            style = MaterialTheme.typography.titleLarge,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.basicMarquee().clickable {
              uriHandler.openUri("https://vocadb.net/S/313646")
            },
          )
        }
      }
      Text(
        text = "Producers feat. Vocal",
        style = MaterialTheme.typography.titleMedium,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.basicMarquee(),
      )
      Text(
        text = "Published at: 2014-08-31",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        modifier = Modifier.basicMarquee(),
      )
      Text(
        text = "Song Type: Original",
        style = MaterialTheme.typography.bodyMedium,
        maxLines = 1,
        modifier = Modifier.basicMarquee(),
      )
      PvRows()
    }
  }
}

@Composable
private fun PvRows() {
  Row(
    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
    verticalAlignment = Alignment.CenterVertically,
  ) {
    val uriHandler = LocalUriHandler.current

    Text(
      text = "PVs:",
      style = MaterialTheme.typography.bodyMedium,
    )
    Icon(
      imageVector = SimpleIcons.Niconico,
      contentDescription = "PV1",
      modifier = Modifier.size(24.dp).clickable {
        uriHandler.openUri("https://www.nicovideo.jp/watch/sm38203271")
      }
    )
    Icon(
      imageVector = SimpleIcons.Youtube,
      contentDescription = "PV2",
      modifier = Modifier.size(24.dp).clickable {
        uriHandler.openUri("https://www.youtube.com/watch?v=W4Rw7to4dig")
      }
    )
  }
}

@Composable
fun ThumbnailImage() {
  Image(
    painter = painterResource("image/√ 甘露とビターシップ _ 初音ミク [sm38203271].jpg"),
    contentDescription = "Thumbnail",
    modifier = Modifier
      .size(120.dp)
      .clip(RoundedCornerShape(MaterialTheme.spacing.cornerShape))
  )
}
