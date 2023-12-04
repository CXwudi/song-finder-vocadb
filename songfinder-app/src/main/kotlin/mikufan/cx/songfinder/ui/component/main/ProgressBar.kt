package mikufan.cx.songfinder.ui.component.main

import androidx.compose.animation.*
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import mikufan.cx.songfinder.backend.controller.mainpage.ProgressBarController
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface


@Composable
fun ProgressBar(
  controller: ProgressBarController = getSpringBean(),
  modifier: Modifier = Modifier
) {
  val currentCountState = controller.currentIndexState
  val totalCount = remember { controller.totalCount }
  RealProgressBar(currentCountState, totalCount, modifier)
}

@Composable
fun RealProgressBar(currentCountState: State<ULong>, totalCount: ULong, modifier: Modifier = Modifier) =
  RowCentralizedWithSpacing {
  TooltipAreaWithCard(
    tip = {
      ProgressTooltipText()
    },
  ) {
    Text("Progress:")
  }
  TooltipAreaWithCard(
    tip = {
      ProgressTooltipText()
    },
    modifier = modifier.weight(1f),
  ) {
    RowCentralizedWithSpacing(modifier = Modifier) {
      BeautifulProgressIndicator((currentCountState.value.toDouble() / totalCount.toDouble()).toFloat())
    }
  }
    BeautifulTextualProgressIndicator(currentCountState, totalCount)
}

@Composable
fun BeautifulTextualProgressIndicator(currentCountState: State<ULong>, totalCount: ULong, modifier: Modifier = Modifier) = Row(
  modifier = modifier,
  verticalAlignment = Alignment.CenterVertically,
) {
  // copied from https://developer.android.com/jetpack/compose/animation/composables-modifiers#animatedcontent
  AnimatedContent(
    targetState = currentCountState.value,
    transitionSpec = {
      // Compare the incoming number with the previous number.
      if (targetState > initialState) {
        // If the target number is larger, it slides up and fades in
        // while the initial (smaller) number slides up and fades out.
        (slideInVertically { height -> height } + fadeIn()) togetherWith slideOutVertically { height -> -height } + fadeOut()
      } else {
        // If the target number is smaller, it slides down and fades in
        // while the initial number slides down and fades out.
        (slideInVertically { height -> -height } + fadeIn()) togetherWith slideOutVertically { height -> height } + fadeOut()
      }.using(
        // Disable clipping since the faded slide-in/out should
        // be displayed out of bounds.
        SizeTransform(clip = false)
      )
    }
  ) { targetCount ->
    Text("$targetCount")
  }
  Text("/$totalCount Songs")
}

@Composable
private fun ProgressTooltipText() {
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
    RealProgressBar(mutableStateOf(39u), 100u)
  }
}
