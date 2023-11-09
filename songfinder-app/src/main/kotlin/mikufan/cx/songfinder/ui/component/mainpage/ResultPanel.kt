package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.runtime.Composable
import mikufan.cx.songfinder.backend.controller.mainpage.ResultPanelController
import mikufan.cx.songfinder.getSpringBean

@Composable
fun ResultPanel() {
  val controller = getSpringBean<ResultPanelController>()
  //TODO
}