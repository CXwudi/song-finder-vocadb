package mikufan.cx.songfinder.backend.config

import io.kamel.core.config.KamelConfig
import io.kamel.core.config.httpFetcher
import io.kamel.core.config.takeFrom
import io.kamel.image.config.Default
import io.kamel.image.config.resourcesFetcher
import io.ktor.client.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class KamelSpringConfig {

  @Bean
  fun kamelConfig(ktorClient: HttpClient) = KamelConfig {
    takeFrom(KamelConfig.Default)
    // Available only on Desktop.
    resourcesFetcher()
    imageBitmapCacheSize = 250
    httpFetcher {
      httpCache(50 * 1024 * 1024  /* 50 MiB */)
      install(ktorClient)
    }
  }
}