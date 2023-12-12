package mikufan.cx.songfinder.ui.component.main

import androidx.compose.runtime.Composable
import mikufan.cx.songfinder.backend.controller.mainpage.ResultOverridingController
import mikufan.cx.songfinder.getSpringBean

@Composable
fun ResultOverridingPanel(
  controller: ResultOverridingController = getSpringBean(),
) {
  val model = ResultOverridingPanelModel(controller::overrideResultAndContinue)
  RealResultOverridingPanel(model)
}

@Composable
fun RealResultOverridingPanel(model: ResultOverridingPanelModel) {
  // TODO
}

data class ResultOverridingPanelModel(
  val onOverride: suspend (ULong) -> Unit,
)