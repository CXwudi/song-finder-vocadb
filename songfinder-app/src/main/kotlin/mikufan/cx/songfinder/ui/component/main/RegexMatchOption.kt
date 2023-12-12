package mikufan.cx.songfinder.ui.component.main

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.onClick
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import mikufan.cx.songfinder.backend.controller.mainpage.RegexMatchOptionController
import mikufan.cx.songfinder.backend.statemodel.SearchRegexOption
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.common.TooltipAreaWithCard
import mikufan.cx.songfinder.ui.theme.spacing

/**
 * Displaying a Regex Match Option UI.
 *
 * @param controller The RegexMatchOptionController object used for managing the state of the Regex Match Option UI.
 *
 */
@Composable
fun RegexMatchOption(
  controller: RegexMatchOptionController = getSpringBean(),
) {
  val model = RegexMatchOptionModel(
    controller.currentRegexOptionState,
    controller.currentInputState,
    controller::setRegexOption
  )
  RealRegexMatchOption(model)
}

/**
 * Composable function that renders the Regex Match Option UI.
 *
 * @param model The RegexMatchOptionModel containing the current state and callbacks for the Regex Match Option.
 * @param modifier The modifier to apply to the UI element.
 */
@Composable
internal fun RealRegexMatchOption(
  model: RegexMatchOptionModel,
  modifier: Modifier = Modifier
) = RowCentralizedWithSpacing(
  horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing)
) {
  Text("Regex Match Option: ")
  RegexMatchOptionButton(SearchRegexOption.Exact, model, modifier)
  RegexMatchOptionButton(SearchRegexOption.StartWith, model, modifier)
  RegexMatchOptionButton(SearchRegexOption.Contains, model, modifier)
}


/**
 * Displays a radio button with a label for a search option
 * in a regular expression search.
 *
 * @param renderedOption The search option to be displayed.
 * @param model The model that contains the current state and event handlers for the search options.
 * @param modifier The modifier to apply to the button.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
internal fun RegexMatchOptionButton(
  renderedOption: SearchRegexOption,
  model: RegexMatchOptionModel,
  modifier: Modifier = Modifier,
) {
  val (currentRegexOptionState, currentInputState, onOptionSet) = model
  val scope = rememberCoroutineScope()
  TooltipAreaWithCard(
    tip = {
      Text("Search using (REGEXP ${renderedOption.pattern.format(currentInputState.value)}) SQL expression")
    }
  ) {
    Row(
      modifier = modifier.onClick { scope.launch { onOptionSet(renderedOption) } },
      verticalAlignment = Alignment.CenterVertically
    ) {
      RadioButton(
        selected = renderedOption == currentRegexOptionState.value,
        onClick = { scope.launch { onOptionSet(renderedOption) } },
      )
      Text(
        text = renderedOption.displayName,
      )
    }
  }
}

/**
 * Represents the model for the [RegexMatchOption].
 *
 * @property currentRegexOptionState The state object representing the current regex option state.
 * @property currentInputState The state object representing the current input state.
 * @property onOptionSet The function to be called when a regex option is set.
 */
data class RegexMatchOptionModel(
  val currentRegexOptionState: State<SearchRegexOption>,
  val currentInputState: State<String>,
  val onOptionSet: suspend (SearchRegexOption) -> Unit,
)
