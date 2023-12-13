package mikufan.cx.songfinder.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.SubcomposeLayout


/**
 * Column with specialized spacing for the first item using [SubcomposeLayout]
 */
@Composable
fun ColumnThatResizesFirstItem(
  modifier: Modifier = Modifier,
  spacing: Int = 0,
  content: @Composable () -> Unit
) {
  // see https://foso.github.io/Jetpack-Compose-Playground/ui/layout/subcomposelayout/
  // and https://developer.android.com/reference/kotlin/androidx/compose/ui/layout/package-summary#SubcomposeLayout(androidx.compose.ui.Modifier,kotlin.Function2)
  SubcomposeLayout(modifier = modifier) { constraints ->
    val placeables = subcompose(ColumnItem.Main, content).map {
      it.measure(constraints.copy(minHeight = 0, minWidth = 0))
    }

    val remainingHeight: Int = constraints.maxHeight - placeables.drop(1).fold(0) { acc, placeable ->
      acc + placeable.height
    } - spacing * (placeables.size - 1)

    val restMeasurables = subcompose(ColumnItem.Rest, content)
    val firstPlaceable =
      restMeasurables.first().measure(constraints.copy(minHeight = remainingHeight, maxHeight = remainingHeight))
    val restPlaceables = restMeasurables.drop(1).map {
      it.measure(constraints.copy(minHeight = 0, minWidth = 0))
    }

    val resizedPlaceables = listOf(firstPlaceable) + restPlaceables

    layout(constraints.maxWidth, constraints.maxHeight) {
      var yPosition = 0
      resizedPlaceables.forEach { placeable ->
        placeable.placeRelative(0, yPosition)
        yPosition += placeable.measuredHeight + spacing
      }
    }
  }
}

enum class ColumnItem {
  Main, Rest
}