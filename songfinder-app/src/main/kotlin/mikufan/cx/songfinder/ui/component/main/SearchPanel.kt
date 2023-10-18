package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.backend.controller.IOController
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard

@Composable
fun SearchPanel(modifier: Modifier = Modifier) {
  val ioController = getSpringBean<IOController>()
  val titleToSearch by ioController.currentLineState
  RealSearchBar(titleToSearch, modifier)
}

@Composable
fun RealSearchBar(title: String, modifier: Modifier = Modifier) = RowCentralizedWithSpacing {
  var inputTitle by remember { mutableStateOf(title) }
  TooltipAreaWithCard(
    tip = {
      Text(
        "Search for the VocaDB record based on the title,\n" +
            "using the VocaDB Dump Database."
      )
    },
    modifier = modifier.weight(1f),
  ) {
    RowCentralizedWithSpacing(
      modifier = Modifier,
      horizontalArrangement = Arrangement.Start
    ) {
      OutlinedTextField(
        value = inputTitle,
        onValueChange = {
          inputTitle = it
          // TODO: do the search in backend
        },
        label = {
          RowCentralizedWithSpacing(
            modifier = Modifier,
            horizontalArrangement = Arrangement.SpaceBetween
          ) {
            Icon(Icons.Default.Search, contentDescription = "Search Icon")
            Text("Search Song Title")
          }
        },
        maxLines = 1,
        modifier = Modifier.weight(1f),
      )
    }

  }
}