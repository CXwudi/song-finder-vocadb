package mikufan.cx.songfinder.backend.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.ktor.client.*
import io.ktor.client.engine.java.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.jackson.*
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration(proxyBeanMethods = false)
class KtorClientConfig {

  @Bean("thumbnail-fetcher")
  @Primary
  fun thumbnailFetcher(
    mapper: ObjectMapper,
    @Qualifier("base-client")
    baseClient: HttpClient,
  ) = HttpClient(Java) {
    install(baseClient)
    install(ContentNegotiation) {
      // use spring jackson mapper
      register(ContentType.Application.Json, JacksonConverter(mapper))
    }
  }

  @Bean("base-client")
  fun baseClient() = HttpClient(Java) {
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
  }
}