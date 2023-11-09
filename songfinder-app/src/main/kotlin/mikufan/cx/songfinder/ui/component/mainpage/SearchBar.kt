package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import mikufan.cx.songfinder.backend.controller.mainpage.SearchBarController
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
  val controller = getSpringBean<SearchBarController>()
  val inputState = controller.currentInputState
  val searchStatusState = controller.currentSearchStatusState
  val searchFunc = controller::search
  val model = SearchBarModel(
    inputState,
    searchStatusState,
    controller::setInput,
  )
  DoSearchComposition(inputState.value, searchFunc)
  RealSearchBar(model, modifier)
}

@Composable
fun DoSearchComposition(value: String, searchFunc: suspend () -> Unit) {
//  val scope = rememberCoroutineScope()
  LaunchedEffect(value) {
//      println("start $value")
//      delay(1300)
//      println("end $value")
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
      SearchTextField(model)
    }
  }
  SearchProgressIndicator(model.searchStatusState.value)
}

@Composable
internal fun RowScope.SearchTextField(
  model: SearchBarModel
) {
  val (currentInputState, _, onValueChange) = model
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

@Composable
fun SearchProgressIndicator(status: SearchStatus) {
  when (status) {
    SearchStatus.Searching -> {
      OnSearchingIndicator()
    }
    SearchStatus.Done -> {
      IsDoneIndicator()
    }
  }
}

@Composable
fun OnSearchingIndicator() {
  TooltipAreaWithCard(
    tip = {
      Text("Searching for the song, please wait...")
    },
  ) {
    CircularProgressIndicator()
  }
}

@Composable
fun IsDoneIndicator() {
  TooltipAreaWithCard(
    tip = {
      Text("Search is done")
    },
  ) {
    Icon(Icons.Default.Done, contentDescription = "Search is Done")
  }
}

data class SearchBarModel(
  val currentInputState: State<String>,
  val searchStatusState: State<SearchStatus>,
  val onValueChange: (newInput: String) -> Unit,
)