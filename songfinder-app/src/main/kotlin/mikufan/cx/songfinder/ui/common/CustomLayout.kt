package mikufan.cx.songfinder.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout

/**
 * A two-item column that resizes its first item to make sure the second item is always visible.
 *
 * @param modifier The modifier to be applied to the [SubcomposeLayout]
 * @param resizibleFirstContent The composable function that draws the first item
 * @param fixedSizeSecondContent The composable function that draws the second item
 */
@Composable
fun ColumnThatResizesFirstItem(
  modifier: Modifier = Modifier,
  resizibleFirstContent: @Composable () -> Unit,
  fixedSizeSecondContent: @Composable () -> Unit
) {
  SubcomposeLayout(modifier = modifier) { constraints ->
    val fixedSizePlacebles = subcompose(Phase.One, fixedSizeSecondContent).map {
      it.measure(constraints.copy(minHeight = 0, minWidth = 0))
    }
    val firstContentMaxHeight = fixedSizePlacebles.maxOfOrNull { it.height } ?: 0
    val remainingHeight: Int = constraints.maxHeight - firstContentMaxHeight

    val resiziblePlacebles = subcompose(Phase.Two, resizibleFirstContent).map {
      it.measure(constraints.copy(minHeight = 0, maxHeight = remainingHeight))
    }

    val yPosition = resiziblePlacebles.maxOfOrNull { it.height } ?: 0

    layout(constraints.maxWidth, constraints.maxHeight) {
      resiziblePlacebles.forEach { placeable ->
        placeable.placeRelative(0, 0)
      }
      fixedSizePlacebles.forEach { placeable ->
        placeable.placeRelative(0, yPosition)
      }
    }
  }
}

private enum class Phase {
  One, Two
}
