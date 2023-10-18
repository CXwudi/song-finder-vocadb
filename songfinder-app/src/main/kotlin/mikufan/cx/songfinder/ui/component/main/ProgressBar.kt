package mikufan.cx.songfinder.ui.component.main

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import mikufan.cx.songfinder.backend.controller.ProgressController
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface


@Composable
fun ProgressBar(modifier: Modifier = Modifier) {
  val controller = getSpringBean<ProgressController>()
  val currentCount by controller.currentCountState
  val totalCount = remember { controller.totalCount }
  RealProgressBar(currentCount, totalCount, modifier)
}

@Composable
fun RealProgressBar(currentCount: ULong, totalCount: ULong, modifier: Modifier = Modifier) = RowCentralizedWithSpacing {
  TooltipAreaWithCard(
    tip = {
      progressTooltipText()
    },
  ) {
    Text("Progress:")
  }
  TooltipAreaWithCard(
    tip = {
      progressTooltipText()
    },
    modifier = modifier.weight(1f),
  ) {
    RowCentralizedWithSpacing(modifier = Modifier) {
      BeautifulProgressIndicator((currentCount.toDouble() / totalCount.toDouble()).toFloat())
    }
  }
  Text("$currentCount/$totalCount Songs")

}

@Composable
private fun progressTooltipText() {
  Text(
    "The progress of the current task,\n" +
        "which is the number of songs that have been processed\n" +
        "divided by the total number of songs to be processed."
  )
}

@Composable
internal fun RowScope.BeautifulProgressIndicator(progress: Float, modifier: Modifier = Modifier) {
  val progressAnimated by animateFloatAsState(
    targetValue = progress,
    animationSpec = spring(
      stiffness = Spring.StiffnessMediumLow,
      visibilityThreshold = 1/1000f
    )
  )
  LinearProgressIndicator(
    progress = progressAnimated,
    modifier = modifier.clip(MaterialTheme.shapes.medium).weight(1f),
    strokeCap = StrokeCap.Round,
  )
}

@Preview
@Composable
fun PreviewProgressBar() {
  MyAppThemeWithSurface {
    RealProgressBar(39u, 100u)
  }
}
