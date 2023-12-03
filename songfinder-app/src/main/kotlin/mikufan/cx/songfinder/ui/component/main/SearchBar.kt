package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import mikufan.cx.songfinder.backend.controller.mainpage.SearchBarController
import mikufan.cx.songfinder.backend.statemodel.SearchStatus
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard

/**
 * Composable function for displaying a search bar.
 *
 * @param modifier The modifier to be applied to the search bar.
 */
@Composable
fun SearchBar(
  controller: SearchBarController = getSpringBean(),
  modifier: Modifier = Modifier
) {
  val inputState = controller.currentInputState
  val searchStatusState = controller.currentSearchStatusState
  val model = SearchBarModel(
    inputState,
    searchStatusState,
    controller::setInput,
    controller::search
  )
  DoSearchAtStart(controller::search)
  RealSearchBar(model, modifier)
}

/**
 * Performs a search operation once upon the app starts.
 *
 * The operation should be performed only once as it is wrapped in a [LaunchedEffect].
 *
 * @param searchFunc the suspend function to execute the search operation.
 */
@Composable
fun DoSearchAtStart(searchFunc: suspend () -> Unit) {
  LaunchedEffect(Unit) {
    searchFunc()
  }
}

/**
 * Displays a real search bar that allows users to search for a VocaDB record based on the title.
 * It utilizes the VocaDB Dump Database for the search.
 *
 * @param model the SearchBarModel used to manage the state of the search bar.
 * @param modifier the modifier to be applied to the search bar.
 */
@Composable
fun RealSearchBar(
  model: SearchBarModel,
  modifier: Modifier = Modifier
) = RowCentralizedWithSpacing {
  TooltipAreaWithCard(
    tip = {
      Text(
        """
          Search for the VocaDB record based on the title,
          using the VocaDB Dump Database.
        """.trimIndent()
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
  Box(modifier = modifier.width(48.dp)) {
    SearchProgressIndicator(model.searchStatusState.value, modifier.align(Alignment.Center))
  }
}

/**
 * A composable method that renders a search text field.
 *
 * @param model The [SearchBarModel] containing the current input state, label, and change handler.
 */
@Composable
internal fun RowScope.SearchTextField(
  model: SearchBarModel
) {
  val scope = rememberCoroutineScope()
  val (currentInputState, _, onValueChange, doSearch) = model
  OutlinedTextField(
    value = currentInputState.value,
    onValueChange = {
      onValueChange(it)
      scope.launch { doSearch() }
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
    modifier = Modifier.fillMaxWidth(),
  )
}

/**
 * Displays a progress indicator based on the given search status.
 *
 * @param status The current search status.
 */
@Composable
internal fun SearchProgressIndicator(status: SearchStatus, alignment: Modifier) {
  when (status) {
    SearchStatus.Searching -> {
      OnSearchingIndicator(alignment)
    }
    SearchStatus.Done -> {
      IsDoneIndicator(alignment)
    }
  }
}

/**
 * Displays a searching indicator with a tooltip and a circular progress indicator.
 *
 * @Composable
 * fun OnSearchingIndicator()
 *
 * When called, the method will display a tooltip area with a "Searching for the song, please wait..." text and a circular progress indicator.
 */
@Composable
internal fun OnSearchingIndicator(alignment: Modifier) {
  TooltipAreaWithCard(
    tip = {
      Text("Searching for the song, please wait...")
    },
    modifier = alignment,
  ) {
    CircularProgressIndicator()
  }
}

/**
 * Composable function that displays an indicator for a completed search.
 *
 * When called, it displays a tooltip area with a card containing the text "Search is done".
 * Additionally, it shows an icon representing the completion of the search.
 */
@Composable
internal fun IsDoneIndicator(alignment: Modifier) {
  TooltipAreaWithCard(
    tip = {
      Text("Search is done")
    },
    modifier = alignment,
  ) {
    Icon(Icons.Default.Done, contentDescription = "Search is Done")
  }
}

/**
 * Represents the model for a search bar component.
 *
 * @property currentInputState The state representing the current input value of the search bar.
 * @property searchStatusState The state representing the search status of the search bar.
 * @property onValueChange The callback function that is invoked when the input value of the search bar changes.
 */
data class SearchBarModel(
  val currentInputState: State<String>,
  val searchStatusState: State<SearchStatus>,
  val onValueChange: (newInput: String) -> Unit,
  val doSearch: suspend () -> Unit,
)