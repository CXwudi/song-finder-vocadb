package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import mikufan.cx.songfinder.backend.controller.mainpage.SearchBarController
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
  val controller = getSpringBean<SearchBarController>()
  val inputState = controller.currentInputState
  val searchFunc = controller::search
  val model = SearchBarModel(
    inputState,
    controller::setInput,
  )
  DoSearch(inputState.value, searchFunc)
  RealSearchBar(model, modifier)
}

@Composable
fun DoSearch(value: String, searchFunc: suspend () -> Unit) {
  LaunchedEffect(value) {
    delay(100) // still do a small delay waiting for user input
    searchFunc()
  }
}

@Composable
fun RealSearchBar(
  model: SearchBarModel,
  modifier: Modifier = Modifier
) = RowCentralizedWithSpacing {
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
      searchTextField(model)
    }
  }
}

@Composable
internal fun RowScope.searchTextField(
  model: SearchBarModel
) {
  val (currentInputState, onValueChange) = model
  OutlinedTextField(
    value = currentInputState.value,
    onValueChange = {
      onValueChange(it)
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

data class SearchBarModel(
  val currentInputState: State<String>,
  val onValueChange: (newInput: String) -> Unit,
)