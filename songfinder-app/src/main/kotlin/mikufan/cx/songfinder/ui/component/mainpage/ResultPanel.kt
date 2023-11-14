package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mikufan.cx.songfinder.backend.controller.mainpage.ResultPanelController
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing

/**
 * Composable function to display the result panel.
 * Retrieves the current result state from the controller and passes it to the RealResultPanel composable.
 */
@Composable
fun ResultPanel() {
  val controller = getSpringBean<ResultPanelController>()
  val resultList = controller.currentResultState
  RealResultPanel(resultList)
}

/**
 * A composable function that displays the search results panel.
 *
 * @param resultList The list of search results.
 * @param modifier The modifier to be applied to the panel.
 */
@Composable
fun RealResultPanel(resultList: List<SongSearchResult>, modifier: Modifier = Modifier) {
  if (resultList.isEmpty()) {
    RowCentralizedWithSpacing {
      Text("No result found", style = MaterialTheme.typography.titleMedium)
    }
  } else {
    ResultPanelGrid(resultList) { result ->
      ResultGridCell(result)
    }
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
  cellContent: @Composable (SongSearchResult) -> Unit,
) {
  LazyVerticalGrid(
    columns = GridCells.Adaptive(256.dp),
  ) {
    items(
      items = resultList,
      key = { result -> result.id }
    ) { result ->
      cellContent(result)
    }
  }
}