package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import mikufan.cx.songfinder.backend.controller.mainpage.ResultPanelController
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.theme.spacing

/**
 * Composable function to display the result panel.
 * Retrieves the current result state from the controller and passes it to the RealResultPanel composable.
 */
@Composable
fun ResultPanel() {
  val controller = getSpringBean<ResultPanelController>()
  val resultList = controller.currentResultState
  RealResultPanel(resultList) { result ->
    ResultGridCell(result)
  }
}

/**
 * A composable function that displays the search results panel.
 *
 * @param resultList The list of search results.
 * @param modifier The modifier to be applied to the panel.
 */
@Composable
fun RealResultPanel(
  resultList: List<SongSearchResult>,
  modifier: Modifier = Modifier,
  cellContent: @Composable LazyGridItemScope.(SongSearchResult) -> Unit,
) {
  if (resultList.isEmpty()) {
    RowCentralizedWithSpacing(furtherModifier = modifier) {
      Text("No result found", style = MaterialTheme.typography.titleMedium)
    }
  } else {
    ResultPanelGrid(resultList, modifier, cellContent)
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
  modifier: Modifier = Modifier,
  cellContent: @Composable LazyGridItemScope.(SongSearchResult) -> Unit,
) {
  val gridState = rememberLazyGridState(0)
//  val verticalBarState = rememberScrollbarAdapter(scrollState = gridState)
//
//  val scrollBarStyle = LocalScrollbarStyle.current.let { style ->
//    if (isSystemInDarkTheme()) {
//      // Reverse the scrollbar color
//      style.copy(
//        unhoverColor = Color.White.copy(style.unhoverColor.alpha),
//        hoverColor = Color.White.copy(style.hoverColor.alpha),
//      )
//    } else {
//      style
//    }
//  }
  // have to use box here to draw the scrollbar on the right side
  Box(
    modifier = modifier.padding(horizontal = MaterialTheme.spacing.padding),
  ) {
    LazyVerticalGrid(
      columns = GridCells.Adaptive(320.dp),
      horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
      verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacingSmall),
//      modifier = modifier.padding(end = MaterialTheme.spacing.paddingLarge),
      state = gridState,
    ) {
      items(
        items = resultList,
        key = { result -> result.id }
      ) { result ->
        cellContent(result)
      }
    }

//    OnNotResizing {
//      VerticalScrollbar(
//        adapter = verticalBarState,
//        modifier = Modifier.align(Alignment.CenterEnd),
//        style = scrollBarStyle,
//      )
//    }
  }
}
//
//@OptIn(ExperimentalComposeUiApi::class, FlowPreview::class)
//@Composable
//fun OnNotResizing(
//  content: @Composable () -> Unit,
//) {
//  val windowInfo = LocalWindowInfo.current
//  val isResizing = remember { mutableStateOf(false) }
//
//  LaunchedEffect(windowInfo) {
//    snapshotFlow { windowInfo.containerSize }
////      .debounce(100)
//      .collect {
//        val newIsResizing = it != windowInfo.containerSize
//        if (newIsResizing != isResizing.value) {
//          isResizing.value = newIsResizing
//        }
//      }
//  }
//
//// do not draw if resizing
//  if (!isResizing.value) {
//    content()
//  }
//}