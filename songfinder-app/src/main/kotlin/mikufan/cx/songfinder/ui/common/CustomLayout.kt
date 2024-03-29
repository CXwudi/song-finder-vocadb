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
  // SubcomposeLayout allows us to measure and place child layouts manually
  // See more guides here:
  // https://foso.github.io/Jetpack-Compose-Playground/ui/layout/subcomposelayout/
  // https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/package-summary#SubcomposeLayout(androidx.compose.ui.Modifier,kotlin.Function2)
  // https://stackoverflow.com/questions/69663194/jetpack-compose-how-does-subcomposelayout-work
  // https://stackoverflow.com/questions/73354911/how-to-get-exact-size-without-recomposition/73357119
  SubcomposeLayout(modifier = modifier) { constraints ->
    // Measure the second item first to ensure it's always visible
    val fixedSizePlacebles = subcompose(Phase.One, fixedSizeSecondContent).map {
      it.measure(constraints.copy(minHeight = 0, minWidth = 0))
    }
    // Calculate the remaining height after placing the second item
    val firstContentMaxHeight = fixedSizePlacebles.maxOfOrNull { it.height } ?: 0
    val remainingHeight: Int = constraints.maxHeight - firstContentMaxHeight

    // Measure the first item with the remaining height
    val resiziblePlacebles = subcompose(Phase.Two, resizibleFirstContent).map {
      it.measure(constraints.copy(minHeight = 0, maxHeight = remainingHeight))
    }

    // Calculate the y position for the second item when placed below the first item
    val yPosition = resiziblePlacebles.maxOfOrNull { it.height } ?: 0

    // Layout the items
    layout(constraints.maxWidth, constraints.maxHeight) {
      // Place the first item at the top
      resiziblePlacebles.forEach { placeable ->
        placeable.placeRelative(0, 0)
      }
      // Place the second item below the first item
      fixedSizePlacebles.forEach { placeable ->
        placeable.placeRelative(0, yPosition)
      }
    }
  }
}

private enum class Phase {
  One, Two
}