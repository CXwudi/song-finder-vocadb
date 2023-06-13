package mikufan.cx.songfinder.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.ui.common.FillInSpacer
import mikufan.cx.songfinder.ui.theme.MyAppTheme

/**
 * @author CX无敌
 * 2023-06-06
 */
@Composable
fun MyApp(
  content: @Composable () -> Unit,
) {
  MyAppTheme {
    Surface(
      modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.background),
    ) {
      content()
    }
  }
}

@Preview
@Composable
fun previewOfRoot() {
  MyApp {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceEvenly,
    ) {
      Button(onClick = {}) {
        Text("Click me")
      }
      Text("Hello World")
      FillInSpacer()
    }
  }
}