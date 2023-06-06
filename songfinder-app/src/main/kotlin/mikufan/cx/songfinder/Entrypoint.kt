package mikufan.cx.songfinder

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.model.IOFiles
import mikufan.cx.songfinder.ui.component.LoadingScreen
import mikufan.cx.songfinder.ui.theme.MyAppTheme

/**
 * @author CX无敌
 * 2023-06-05
 */
private val log = KInlineLogging.logger()
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

@Composable
fun MyApp(
  content: @Composable () -> Unit,
) {
  MyAppTheme {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colors.background)
        .padding(5.dp),
    ) {
      content()
    }
  }
}

@Preview
@Composable
fun previewOfRoot() {
  MyApp {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.spacedBy(5.dp),
    ) {
      Text("Hello World")
      Button(onClick = {}) {
        Text("Click me")
      }
    }
  }
}
