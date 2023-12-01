package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.ui.theme.spacing

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridItemScope.ResultGridCell(result: SongSearchResult) {
  ResultGridCell(result, Modifier.animateItemPlacement())
}

@Composable
fun DebugUsedCell(result: SongSearchResult, modifier: Modifier = Modifier) {
}

@Composable
fun LazyGridItemScope.ResultGridCell(result: SongSearchResult, modifier: Modifier = Modifier) {
  MusicCardTemplate({}, modifier) {
    Text(result.toString())
  }
}

/* --- Utils ---*/


@Composable
fun MusicCardTemplate(onCardClicked: suspend () -> Unit, modifier: Modifier = Modifier, content: @Composable () -> Unit) {
  val scope = rememberCoroutineScope()
  Card(
    shape = RoundedCornerShape(MaterialTheme.spacing.cornerShapeLarge),
    modifier = Modifier.then(modifier).clickable {
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