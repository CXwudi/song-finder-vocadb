package mikufan.cx.songfinder.ui.test

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.httpFetcher
import io.kamel.core.config.takeFrom
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.kamel.image.config.Default
import io.kamel.image.config.LocalKamelConfig
import io.kamel.image.config.resourcesFetcher
import io.ktor.client.plugins.*
import io.ktor.http.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import mikufan.cx.songfinder.ui.theme.MyAppTheme

fun main() = singleWindowApplication {
  MyAppTheme(darkTheme = true) {
    val desktopConfig = KamelConfig {
      takeFrom(KamelConfig.Default)
      // Available only on Desktop.
      resourcesFetcher()
      imageBitmapCacheSize = 250
      // by adding this http fetcher, we lose the ability to get image from resources
      httpFetcher {
        install(UserAgent) {
          agent = "Song Finder by VocaDB @ https://github.com/CXwudi/song-finder-vocadb"
        }
        install(HttpRequestRetry) {
          maxRetries = 1
          retryIf { _, httpResponse ->
            !httpResponse.status.isSuccess()
          }
        }

        followRedirects = true
        httpCache(50 * 1024 * 1024  /* 50 MiB */)

      }
    }
    CompositionLocalProvider(LocalKamelConfig provides desktopConfig) {
      Row {
        Draw()
        Text("Test KamelImage")
      }
    }
  }
}

@Composable
fun Draw() {
  var imageSrc: String by remember { mutableStateOf("image/image-load-failed.svg") }
  LaunchedEffect(true) {
    delay(2000)
    imageSrc = "image/image-not-found-icon.svg"
  }
  KamelImage(
    asyncPainterResource("image/image-load-failed.svg") {
      coroutineContext += Dispatchers.IO
      requestBuilder {

      }
    },
    contentDescription = "A image",
    modifier = Modifier,
    onLoading = { CircularProgressIndicator() },
    onFailure = { e -> println(e) }
  )

}