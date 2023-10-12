package mikufan.cx.songfinder.ui.component.main

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun FinishMessagePanel(modifier: Modifier = Modifier) {
  val bodyLarge = MaterialTheme.typography.bodyLarge
  Text(
    "All Done!",
    fontSize = bodyLarge.fontSize,
    fontFamily = bodyLarge.fontFamily,
    fontWeight = bodyLarge.fontWeight,
  )
}