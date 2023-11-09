package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import mikufan.cx.songfinder.backend.controller.MainScreenController
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.ColumnCentralizedWithSpacing
import mikufan.cx.songfinder.ui.component.mainpage.*
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface

@Composable
fun MainScreen() {
  val finishedState = getSpringBean<MainScreenController>().isFinishedState
  RealMainScreen(finishedState)
}


@Composable
fun RealMainScreen(isAllFinished: State<Boolean>) = ColumnCentralizedWithSpacing {
  ProgressBar()
  Divider()
  RestOfPart(isAllFinished.value)
}

@Composable
fun RestOfPart(isAllFinished: Boolean) = if (isAllFinished) {
  FinishMessagePanel()
} else {
  SearchBar()
  ResultPanel()
}


@Preview
@Composable
fun PreviewMainScreen() {
  MyAppThemeWithSurface {
    ColumnCentralizedWithSpacing {
      RealProgressBar(39u, 100u)
      Divider()
      RealSearchBar(
        SearchBarModel(
          mutableStateOf(""),
          mutableStateOf(SearchStatus.Done),
          {},
        )
      )
    }
  }
}