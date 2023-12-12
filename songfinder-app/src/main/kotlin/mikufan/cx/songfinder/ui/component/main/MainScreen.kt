package mikufan.cx.songfinder.ui.component.main

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import mikufan.cx.songfinder.backend.controller.MainScreenController
import mikufan.cx.songfinder.backend.db.entity.PvService
import mikufan.cx.songfinder.backend.db.entity.PvType
import mikufan.cx.songfinder.backend.db.entity.SongType
import mikufan.cx.songfinder.backend.model.PVInfo
import mikufan.cx.songfinder.backend.model.SongSearchResult
import mikufan.cx.songfinder.backend.model.ThumbnailInfo
import mikufan.cx.songfinder.backend.statemodel.SearchRegexOption
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.ColumnCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.FillInSpacer
import mikufan.cx.songfinder.ui.theme.MyAppTheme
import java.time.LocalDateTime

@Composable
fun MainScreen(
  mainScreenController: MainScreenController = getSpringBean(),
) {
  val finishedState = mainScreenController.isFinishedState
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
    FillInSpacer()
    Divider()
    ResultOverridingPanel()
  })
}

@Composable
fun ColumnScope.RestOfPart(
  isAllFinished: Boolean,
  isAllFinishedContent: @Composable ColumnScope.() -> Unit,
  notFinishedContent: @Composable ColumnScope.() -> Unit
) =
  if (isAllFinished) {
    isAllFinishedContent()
  } else {
    notFinishedContent()
  }


@Preview
@Composable
fun PreviewMainScreen() {
  MyAppTheme {
    ColumnCentralizedWithSpacing {
      RealProgressBar(mutableStateOf(39u), 100u)
      Divider()
      RestOfPart(false, {}) {
        RealSearchBar(
          SearchBarModel(
            mutableStateOf(""),
            mutableStateOf(SearchStatus.Done),
            {},
            {}
          )
        )
        RealRegexMatchOption(RegexMatchOptionModel(mutableStateOf(SearchRegexOption.Exact), mutableStateOf("title"), {}))
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
          RealResultGridCell(it, ResultCellCallbacks({}, { Result.success(ThumbnailInfo("url", {})) }))
        }
        FillInSpacer()
        Divider()
        RealResultOverridingPanel(ResultOverridingPanelModel({}))
      }
    }
  }
}