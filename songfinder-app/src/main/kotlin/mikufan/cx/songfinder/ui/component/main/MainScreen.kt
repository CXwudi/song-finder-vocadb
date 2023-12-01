package mikufan.cx.songfinder.ui.component.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.backend.controller.MainScreenController
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.db.entity.PvType
import mikufan.cx.songfinder.backend.db.entity.SongType
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.statemodel.SearchRegexOption
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.ColumnCentralizedWithSpacing
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import java.time.LocalDateTime

@Composable
fun MainScreen() {
  val finishedState = getSpringBean<MainScreenController>().isFinishedState
  RealMainScreen(finishedState)
}


@Composable
fun RealMainScreen(isAllFinished: State<Boolean>) = ColumnCentralizedWithSpacing(
  horizontalAlignment = Alignment.Start
) {
  ProgressBar()
  Divider()
  RestOfPart(isAllFinished.value, { FinishMessagePanel() }, {
    SearchBar()
    RegexMatchOption()
    ResultPanel()
  })
}

@Composable
fun RestOfPart(isAllFinished: Boolean, isAllFinishedContent: @Composable () -> Unit, notFinishedContent: @Composable () -> Unit) =
  if (isAllFinished) {
    isAllFinishedContent()
  } else {
    notFinishedContent()
  }


@OptIn(ExperimentalFoundationApi::class)
@Preview
@Composable
fun PreviewMainScreen() {
  MyAppThemeWithSurface {
    ColumnCentralizedWithSpacing {
      RealProgressBar(39u, 100u)
      Divider()
      RestOfPart(false, {}) {
        RealSearchBar(
          SearchBarModel(
            mutableStateOf(""),
            mutableStateOf(SearchStatus.Done),
            {},
          )
        )
        RealRegexMatchOption(SearchRegexOption.Exact, {})
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
        ) {
          DebugUsedCell(it, Modifier.animateItemPlacement())
        }
      }
    }
  }
}