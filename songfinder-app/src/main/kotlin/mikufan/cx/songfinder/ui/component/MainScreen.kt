package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import mikufan.cx.songfinder.SpringCtx
import mikufan.cx.songfinder.backend.controller.ProgressController
import mikufan.cx.songfinder.ui.common.ColumnCentralizedWithSpacing
import mikufan.cx.songfinder.ui.component.main.FinishMessagePanel
import mikufan.cx.songfinder.ui.component.main.ProgressBar
import mikufan.cx.songfinder.ui.component.main.RealProgressBar
import mikufan.cx.songfinder.ui.component.main.SearchPanel
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import org.springframework.beans.factory.getBean

@Composable
fun MainScreen() {
  val finished = SpringCtx.current.getBean<ProgressController>().isFinished
  RealMainScreen(finished)
}


@Composable
fun RealMainScreen(isAllFinished: Boolean) = ColumnCentralizedWithSpacing {
  ProgressBar()
  Divider()
  if (isAllFinished) {
    FinishMessagePanel()
  } else {
    SearchPanel()
  }

}


@Preview
@Composable
fun PreviewMainScreen() {
  MyAppThemeWithSurface {
    ColumnCentralizedWithSpacing {
      RealProgressBar(39u, 100u)
      Divider()
    }
  }
}