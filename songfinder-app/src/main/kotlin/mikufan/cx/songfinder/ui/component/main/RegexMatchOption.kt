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
 * Composable function for handling regex match options.
 *
 */
@Composable
fun RegexMatchOption(
  controller: RegexMatchOptionController = getSpringBean(),
  modifier: Modifier = Modifier,
) {
  RealRegexMatchOption(controller.currentRegexOptionState, controller.currentInputState, modifier, controller::setRegexOption)
}


/**
 * Composable function to render a row of regex match options.
 *
 * @param option The currently selected search regex option.
 * @param onOptionSet The callback function called when a regex option is selected.
 */
@Composable
fun RealRegexMatchOption(
  option: State<SearchRegexOption>,
  currentInputState: State<String>,
  modifier: Modifier = Modifier,
  onOptionSet: suspend (SearchRegexOption) -> Unit
) = RowCentralizedWithSpacing(
  horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing)
) {
  Text("Regex Match Option: ")
  RegexMatchOptionButton(SearchRegexOption.Exact, option, currentInputState, modifier, onOptionSet)
  RegexMatchOptionButton(SearchRegexOption.StartWith, option, currentInputState, modifier, onOptionSet)
  RegexMatchOptionButton(SearchRegexOption.Contains, option, currentInputState, modifier, onOptionSet)
}

/**
 * Composable function to render a single regex match option.
 *
 * @param renderedOption The regex option to render.
 * @param selectedOptionState The currently selected regex option.
 * @param onOptionSet The callback function called when a regex option is selected.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegexMatchOptionButton(
  renderedOption: SearchRegexOption,
  selectedOptionState: State<SearchRegexOption>,
  currentInputState: State<String>,
  modifier: Modifier,
  onOptionSet: suspend (SearchRegexOption) -> Unit
) {
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
        selected = renderedOption == selectedOptionState.value,
        onClick = { scope.launch { onOptionSet(renderedOption) } },
      )
      Text(
        text = renderedOption.displayName,
      )
    }
  }
}
