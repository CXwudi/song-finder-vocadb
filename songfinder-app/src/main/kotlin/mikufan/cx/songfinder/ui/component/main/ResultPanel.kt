package mikufan.cx.songfinder.ui.component.main

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mikufan.cx.songfinder.backend.controller.mainpage.ResultPanelController
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.theme.spacing

/**
 * Composable function to display the result panel.
 * Retrieves the current result state from the controller and passes it to the RealResultPanel composable.
 */
@Composable
fun ColumnScope.ResultPanel(
  controller: ResultPanelController = getSpringBean<ResultPanelController>(),
  modifier: Modifier = Modifier,
) {
  val resultList = controller.currentResultState
  val gridState = controller.resultGridState
  val scope = rememberCoroutineScope()
  RealResultPanel(resultList, gridState, modifier) { result ->
    ResultGridCell(result, scope)
  }
}

/**
 * A composable function that displays the search results panel.
 *
 * @param resultList The list of search results.
 * @param modifier The modifier to be applied to the panel.
 */
@Composable
fun ColumnScope.RealResultPanel(
  resultList: List<SongSearchResult>,
  gridState: LazyGridState = rememberLazyGridState(),
  modifier: Modifier = Modifier,
  cellContent: @Composable LazyGridItemScope.(SongSearchResult) -> Unit,
) {
  if (resultList.isEmpty()) {
    Row(
      horizontalArrangement = Arrangement.Center,
      verticalAlignment = Alignment.CenterVertically,
      modifier = modifier.padding(horizontal = MaterialTheme.spacing.padding).fillMaxWidth()
    ) {
      Text(
        "No result found",
        style = MaterialTheme.typography.titleMedium,
      )
    }
  } else {
    ResultPanelGrid(resultList, gridState, modifier, cellContent)
  }
}

/**
 * Displays a grid of results using the Jetpack Compose `LazyVerticalGrid`.
 *
 * @param resultList The list of `SongSearchResult` to display.
 * @param cellContent The composable lambda function to render each cell of the grid.
 */
@Composable
fun ResultPanelGrid(
  resultList: List<SongSearchResult>,
  gridState: LazyGridState,
  modifier: Modifier = Modifier,
  cellContent: @Composable LazyGridItemScope.(SongSearchResult) -> Unit,
) {
  Box(
    modifier = modifier.padding(horizontal = MaterialTheme.spacing.padding)
  ) {
    LazyVerticalGrid(
      columns = GridCells.Adaptive(320.dp),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
//      modifier = modifier.padding(end = MaterialTheme.spacing.paddingLarge),
      modifier = modifier.animateContentSize(),
      state = gridState,
    ) {
      items(
        items = resultList,
        key = { result -> result.id }
      ) { result ->
        cellContent(result)
      }
    }
  }
}
