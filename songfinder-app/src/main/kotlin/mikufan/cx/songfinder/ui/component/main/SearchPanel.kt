package mikufan.cx.songfinder.ui.component.main

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.SpringCtx
import mikufan.cx.songfinder.backend.controller.IOController
import org.springframework.beans.factory.getBean

@Composable
fun SearchPanel(modifier: Modifier = Modifier) {
  SpringCtx.current.getBean<IOController>()
  // TODO: implement SearchPanel
}