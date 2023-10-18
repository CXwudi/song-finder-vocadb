package mikufan.cx.songfinder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
class SongFinderSpringBootApp

val SpringCtx = staticCompositionLocalOf<ConfigurableApplicationContext> {
  error("Spring Context is not initialized yet")
}

@Composable
inline fun <reified T> getSpringBean(): T = SpringCtx.current.getBean()