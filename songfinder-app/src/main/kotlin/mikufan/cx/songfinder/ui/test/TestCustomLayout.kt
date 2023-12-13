package mikufan.cx.songfinder.ui.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.singleWindowApplication
import mikufan.cx.songfinder.ui.common.ColumnThatResizesFirstItem
import mikufan.cx.songfinder.ui.theme.MyAppTheme

fun main(args: Array<String>) = singleWindowApplication {
  MyAppTheme(darkTheme = true) {
    DrawColumn()
  }
}

@Composable
fun DrawColumn() {
  Column {
    ColumnThatResizesFirstItem(modifier = Modifier.fillMaxSize(), spacing = 10) {
      SampleLazyColumn()
      Divider()
      Text("Should see this text")
    }
  }

}

@Composable
fun SampleLazyColumn() {
  // draw 100 numbers
  LazyColumn {
    items(35) {
      Text(it.toString())
    }
  }
}