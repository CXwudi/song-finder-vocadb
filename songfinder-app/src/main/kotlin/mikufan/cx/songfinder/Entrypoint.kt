package mikufan.cx.songfinder

import androidx.compose.runtime.*
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.ui.component.input.InputScreen
import mikufan.cx.songfinder.ui.component.loading.LoadingWindow
import mikufan.cx.songfinder.ui.component.main.MainScreen
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import org.springframework.context.ConfigurableApplicationContext
import kotlin.io.path.Path

/**
 * @author CX无敌
 * 2023-06-05
 */
fun main(vararg args: String) = application {
  var targetFiles: IOFiles? by remember { mutableStateOf(null) }

  if (targetFiles == null) {
    launchInputApplication { targetFiles = it }
  } else {
    launchMainApplication(targetFiles, args)
  }
}

@Composable
private fun ApplicationScope.launchInputApplication(onTargetFilesReady: (IOFiles) -> Unit) {
  Window(
    onCloseRequest = ::exitApplication,
    title = "Loading Input and Output Files",
  ) {
    MyAppThemeWithSurface {
      InputScreen(onReady = onTargetFilesReady)
    }
  }
}

@Composable
private fun ApplicationScope.launchMainApplication(
  targetFiles: IOFiles?,
  args: Array<out String>
) {
  var springCtx: ConfigurableApplicationContext? by remember { mutableStateOf(null) }
  if (springCtx == null) {
    LoadingWindow(targetFiles!!, args) { springCtx = it }
  } else {
    val desktopConfig = KamelConfig {
      takeFrom(KamelConfig.Default)
      // Available only on Desktop.
      resourcesFetcher()
      imageBitmapCacheSize = 250
    }
    Window(
      title = "Song Finder powered by VocaDB",
      state = rememberWindowState(size = DpSize(1280.dp, 900.dp)),
      onCloseRequest = {
        springCtx!!.close()
        exitApplication()
      },
    ) {
      MyAppThemeWithSurface {
        CompositionLocalProvider(
          SpringCtx provides springCtx!!,
          LocalKamelConfig provides desktopConfig
        ) {
          MainScreen()
        }
      }
    }
  }
}

object QuickEntryPointForTest {
  @JvmStatic
  fun main(args: Array<String>) = application {
    launchMainApplication(
      IOFiles(
        Path("src/test/resources/test-data/2014 Vocaloid List.txt"),
        0u,
        Path("src/test/resources/test-data/dummy-output.csv")
      ),
      args
    )
  }
}