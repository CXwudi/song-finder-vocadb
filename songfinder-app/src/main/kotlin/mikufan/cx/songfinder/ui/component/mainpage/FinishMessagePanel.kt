package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FinishMessagePanel(modifier: Modifier = Modifier) {
  Text(
    "All Done!",
    style = MaterialTheme.typography.titleMedium,
  )
}