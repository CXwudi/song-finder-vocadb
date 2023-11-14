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

/**
 * Composable function for displaying a search bar.
 *
 * @param modifier The modifier to be applied to the search bar.
 */
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

/**
 * Performs a search operation after a small delay for user input.
 *
 * @param value the search value provided by the user.
 * @param searchFunc the suspend function to execute the search operation.
 */
@Composable
fun DoSearchComposition(value: String, searchFunc: suspend () -> Unit) {
//  val scope = rememberCoroutineScope()
  LaunchedEffect(value) {
//      println("start $value")
//      delay(1300)
//      println("end $value")
    delay(500) // still do a small delay waiting for user input
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
  SearchProgressIndicator(model.searchStatusState.value)
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

/**
 * Displays a progress indicator based on the given search status.
 *
 * @param status The current search status.
 */
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

/**
 * Displays a searching indicator with a tooltip and a circular progress indicator.
 *
 * @Composable
 * fun OnSearchingIndicator()
 *
 * When called, the method will display a tooltip area with a "Searching for the song, please wait..." text and a circular progress indicator.
 */
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

/**
 * Composable function that displays an indicator for a completed search.
 *
 * When called, it displays a tooltip area with a card containing the text "Search is done".
 * Additionally, it shows an icon representing the completion of the search.
 */
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
)