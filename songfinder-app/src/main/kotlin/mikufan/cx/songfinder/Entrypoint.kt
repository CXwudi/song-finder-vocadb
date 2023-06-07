package mikufan.cx.songfinder

import androidx.compose.runtime.*
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.model.IOFiles
import mikufan.cx.songfinder.ui.MyApp
import mikufan.cx.songfinder.ui.component.LoadingScreen
import org.springframework.boot.autoconfigure.SpringBootApplication

/**
 * @author CX无敌
 * 2023-06-05
 */
fun main() = application {
  var targetFiles: IOFiles? by remember { mutableStateOf(null) }

  if (targetFiles == null) {
    Window(
      onCloseRequest = ::exitApplication,
      title = "Loading Input and Output Files",
    ) {
      MyApp {
        LoadingScreen(onReady = { targetFiles = it })
      }
    }
  } else {
    log.info { "Gotten files: $targetFiles" }
  }
}

@SpringBootApplication
class SongFinderSpringBootApp

private val log = KInlineLogging.logger()
