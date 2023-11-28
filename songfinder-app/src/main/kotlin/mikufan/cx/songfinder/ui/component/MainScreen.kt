package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import mikufan.cx.songfinder.backend.controller.MainScreenController
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.db.entity.PvType
import mikufan.cx.songfinder.backend.db.entity.SongType
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.ColumnCentralizedWithSpacing
import mikufan.cx.songfinder.ui.component.mainpage.*
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import java.time.LocalDateTime

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
      RealResultPanel(
        listOf(
          SongSearchResult(
            id = 123L,
            title = "title",
            type = SongType.Original,
            vocals = listOf("vocal1", "vocal2"),
            producers = listOf("producer1", "producer2"),
            publishDate = LocalDateTime.now(),
            pvs = listOf(
              PVInfo(
                id = "sm123",
                pvService = PvService.NicoNicoDouga,
                pvType = PvType.Original,
              )
            )
          )
        )
      )
    }
  }
}