package mikufan.cx.songfinder

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mikufan.cx.songfinder.ui.component.LoadingScreen
import mikufan.cx.songfinder.ui.theme.MyAppTheme

/**
 * @author CX无敌
 * 2023-06-05
 */

fun main() = application {
  var initialLoaded by remember { mutableStateOf(false) }

  if (!initialLoaded) {
    Window(
      onCloseRequest = ::exitApplication,
      title = "Loading...",
    ) {
      MyAppTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
          LoadingScreen(onStarted = {})
        }
      }
    }
  }
}
