package mikufan.cx.songfinder.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mikufan.cx.inlinelogging.KInlineLogging
import mikufan.cx.songfinder.SongFinderSpringBootApp
import mikufan.cx.songfinder.backend.model.IOFiles
import mikufan.cx.songfinder.ui.theme.MyAppThemeWithSurface
import mikufan.cx.songfinder.ui.theme.spacing
import org.springframework.beans.factory.getBean
import org.springframework.boot.runApplication
import org.springframework.context.ConfigurableApplicationContext

/**
 * @author CX无敌
 * 2023-06-15
 */

@Composable
fun ApplicationScope.LoadingWindow(ioFiles: IOFiles, args: Array<out String>, onReady: (ConfigurableApplicationContext) -> Unit) {
  var showLoading by remember { mutableStateOf(false) }
  LaunchedEffect(Unit) {
    launch {
      delay(2000)
      showLoading = true
    }
    val springCtx = launchSpringBootApp(ioFiles, args)
//    delay(5000) // for demo purpose
    log.debug { "All beans: ${springCtx.beanDefinitionNames.joinToString(prefix = "[", postfix = "]")}" }
    log.debug { "IOFiles: ${springCtx.getBean<IOFiles>()}" }
    onReady(springCtx)
  }
  if (showLoading) {
    Window(
      onCloseRequest = ::exitApplication,
      title = "Loading App",
      state = rememberWindowState(width = Dp.Unspecified, height = Dp.Unspecified),
    ) {
      MyAppThemeWithSurface(surfaceModifier = Modifier.background(MaterialTheme.colorScheme.background)) {
        LoadingScreen()
      }
    }
  }
}

fun launchSpringBootApp(ioFiles: IOFiles, args: Array<out String>): ConfigurableApplicationContext =
  runApplication<SongFinderSpringBootApp>(*args) {
    addInitializers({
      it.beanFactory.registerSingleton("ioFiles", ioFiles)
    })
  }

@Composable
fun LoadingScreen() = Row(
  verticalAlignment = Alignment.CenterVertically,
  horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing),
  modifier = Modifier.padding(MaterialTheme.spacing.padding),
) {
  CircularProgressIndicator()
  Text("Loading...")
}

@Preview
@Composable
fun previewOfLoadingWindow() {
  MyAppThemeWithSurface(surfaceModifier = Modifier.background(MaterialTheme.colorScheme.background)) {
    LoadingScreen()
  }
}

private val log = KInlineLogging.logger()
