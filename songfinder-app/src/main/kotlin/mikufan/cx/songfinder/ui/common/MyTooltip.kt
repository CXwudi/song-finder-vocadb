package mikufan.cx.songfinder.ui.common

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.TooltipArea
import androidx.compose.foundation.TooltipPlacement
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * @author CX无敌
 * 2023-06-06
 */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooltipAreaWithCard(
  delayMillis: Int = 500,
  tipModifier: Modifier = Modifier.shadow(4.dp),
  tip: @Composable () -> Unit,
  content: @Composable () -> Unit,
) {
  TooltipArea(
    tooltip = {
      Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = tipModifier,
        shape = MaterialTheme.shapes.small,
      ) {
        tip()
      }
    },
    tooltipPlacement = TooltipPlacement.CursorPoint(
      alignment = Alignment.BottomEnd,
      offset = DpOffset(x = 10.dp, y = 0.dp),
    ),
    delayMillis = delayMillis,
  ) {
    content()
  }
}

@Preview
@Composable
fun PreviewTooltipAreaWithCard() {
  TooltipAreaWithCard(tip = { Text("hello") }) {
    Text("world")
  }
}
