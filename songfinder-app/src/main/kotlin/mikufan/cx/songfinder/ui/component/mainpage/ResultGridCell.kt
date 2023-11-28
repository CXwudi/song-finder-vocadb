package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.lazy.grid.LazyGridItemScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.backend.model.SongSearchResult

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyGridItemScope.ResultGridCell(result: SongSearchResult) {
  DebugUsedCell(result, Modifier.animateItemPlacement())
}

@Composable
fun DebugUsedCell(result: SongSearchResult, modifier: Modifier = Modifier) {
  Text(result.toString(), modifier = modifier)
}