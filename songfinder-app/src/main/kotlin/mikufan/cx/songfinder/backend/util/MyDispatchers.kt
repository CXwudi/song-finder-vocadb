package mikufan.cx.songfinder.backend.util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.asCoroutineDispatcher
import java.util.concurrent.Executors

object MyDispatchers {
  val ioDispatcher = LoomDispatcher

  @OptIn(ExperimentalCoroutinesApi::class)
  val defaultDispatcher = LoomDispatcher.limitedParallelism(Runtime.getRuntime().availableProcessors())
}

private val LoomThreadFactory = Thread.ofVirtual().name("App-VThread-", 0).factory()
private val LoomDispatcher = Executors.newThreadPerTaskExecutor(LoomThreadFactory).asCoroutineDispatcher()