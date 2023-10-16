package mikufan.cx.songfinder

import androidx.compose.runtime.*
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.ui.component.InputScreen
import mikufan.cx.songfinder.ui.component.LoadingWindow
import mikufan.cx.songfinder.ui.component.MainScreen
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext
import kotlin.io.path.Path

/**
 * @author CX无敌
 * 2023-06-05
 */
fun main(vararg args: String) = application {
  var targetFiles: IOFiles? by remember { mutableStateOf(null) }

  if (targetFiles == null) {
    Window(
      onCloseRequest = ::exitApplication,
      title = "Loading Input and Output Files",
    ) {
      MyAppThemeWithSurface {
        InputScreen(onReady = { targetFiles = it })
      }
    }
  } else {
    launchMainApplication(targetFiles, args)
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
    Window(
      onCloseRequest = {
        springCtx!!.close()
        exitApplication()
      },
      title = "Song Finder powered by VocaDB",
    ) {
      MyAppThemeWithSurface {
        CompositionLocalProvider(SpringCtx provides springCtx!!) {
          MainScreen()
        }
      }
    }
  }
}

@SpringBootApplication
class SongFinderSpringBootApp

val SpringCtx = staticCompositionLocalOf<ConfigurableApplicationContext> {
  error("Spring Context is not initialized yet")
}

object QuickEntryPointForTest {
  @JvmStatic
  fun main(args: Array<String>) = application {
    launchMainApplication(
      IOFiles(
        Path("src/test/resources/test-data/dummy-input.txt"),
        0u,
        Path("src/test/resources/test-data/dummy-output.csv")
      ),
      args
    )
  }
}