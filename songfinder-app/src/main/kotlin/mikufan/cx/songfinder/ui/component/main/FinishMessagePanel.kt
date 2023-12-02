package mikufan.cx.songfinder.ui.component.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow

@Composable
fun FinishMessagePanel(modifier: Modifier = Modifier) {
  Text(
    "All Done!",
    style = MaterialTheme.typography.titleMedium,
    overflow = TextOverflow.Ellipsis,
  )
}