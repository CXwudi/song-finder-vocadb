package mikufan.cx.songfinder.backend.config

import io.kamel.core.config.Core
import io.kamel.core.config.KamelConfig
import io.kamel.core.config.httpUrlFetcher
import io.kamel.core.config.takeFrom
import io.kamel.image.config.resourcesFetcher
import io.ktor.client.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration(proxyBeanMethods = false)
class KamelSpringConfig {

  @Bean("kamel-config-for-remote")
  fun kamelConfig(@Qualifier("base-client") baseClient: HttpClient) = KamelConfig {
    takeFrom(KamelConfig.Core)
    // Available only on Desktop.
    resourcesFetcher()
    imageBitmapCacheSize = 250
    httpUrlFetcher {
      install(baseClient)
      httpCache(50 * 1024 * 1024  /* 50 MiB */)
    }
  }
}