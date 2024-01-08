package mikufan.cx.songfinder.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.singleWindowApplication
import mikufan.cx.songfinder.ui.theme.MyAppTheme

fun main() = singleWindowApplication {
  MyAppTheme {
    MyContent()
  }
}

@Composable
private fun MyContent() {
  var count1 by remember { mutableStateOf(0) }
  var count2 by remember { mutableStateOf(0) }
  Row(modifier = Modifier.padding(10.dp)) {
    MyCard(count1) {
      count1++
    }
    MyCard(count2) {
      count2++
    }
  }
}

@Composable
private fun MyCard(count: Int, onClick: () -> Unit) {
  val anotherValue by remember { mutableStateOf(count) }
//  val anotherValue by mutableStateOf(count)
  Card {
    Button(onClick = onClick) {
      Column {
        Text("Clicked $count times")
        Text("Another value: $anotherValue")
      }
    }
  }
}