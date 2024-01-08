package mikufan.cx.songfinder

import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf
import org.springframework.beans.factory.getBean
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.context.ConfigurableApplicationContext

@SpringBootApplication
@EnableCaching
class SongFinderSpringBootApp

val SpringCtx = staticCompositionLocalOf<ConfigurableApplicationContext> {
  error("Spring Context is not initialized yet")
}

@ReadOnlyComposable
@Composable
inline fun <reified T> getSpringBean(): T = SpringCtx.current.getBean()

@Composable
inline fun <reified T> getSpringBeanAndRemember(): T {
  val bean = getSpringBean<T>()
  return remember { bean }
}