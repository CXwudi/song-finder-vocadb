package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import mikufan.cx.songfinder.backend.model.SongSearchResult

@Composable
fun ResultGridCell(result: SongSearchResult) {
  DebugUsedCell(result)
}

@Composable
private fun DebugUsedCell(result: SongSearchResult) {
  Text(result.toString())
}