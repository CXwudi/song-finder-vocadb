package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.SpringCtx
import mikufan.cx.songfinder.backend.controller.IOController
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import org.springframework.beans.factory.getBean

@Composable
fun SearchPanel(modifier: Modifier = Modifier) {
  val ioController = SpringCtx.current.getBean<IOController>()
  val titleToSearch by ioController.currentLineState
  RealSearchBar(titleToSearch, modifier)
}

@Composable
fun RealSearchBar(title: String, modifier: Modifier = Modifier) = RowCentralizedWithSpacing {
  var title by remember { mutableStateOf(title) }
  TooltipAreaWithCard(
    tip = {
      Text(
        "Search for the VocaDB record based on the title,\n " +
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
        value = title,
        onValueChange = {
          title = it
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