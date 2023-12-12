package mikufan.cx.songfinder.ui.test

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mikufan.cx.songfinder.ui.common.ColumnWithSpacing
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface


fun main() = application {
  Window(onCloseRequest = ::exitApplication) {
    MyAppThemeWithSurface {
      var progress by remember { mutableStateOf(0.5f) }
      ColumnWithSpacing {
        BeautifulProgressIndicator(progress)
        RowCentralizedWithSpacing {
          Button(onClick = { progress += 0.05f }) { Text("+ 5%") }
          Button(onClick = { progress -= 0.05f }) { Text("- 5%") }
        }
      }
    }
  }
}

@Composable
internal fun BeautifulProgressIndicator(progress: Float) {
  val progressAnimated by animateFloatAsState(
    targetValue = progress,
    animationSpec = //ProgressIndicatorDefaults.ProgressAnimationSpec
    spring(
      stiffness = Spring.StiffnessMediumLow,
      visibilityThreshold = 1/1000f
    )
//    tween(
//      durationMillis = 100,
//      easing = LinearEasing,
//    )
  )
  LinearProgressIndicator(
    progress = progressAnimated,
    modifier = Modifier.clip(MaterialTheme.shapes.medium),
    strokeCap = StrokeCap.Round,
  )
}