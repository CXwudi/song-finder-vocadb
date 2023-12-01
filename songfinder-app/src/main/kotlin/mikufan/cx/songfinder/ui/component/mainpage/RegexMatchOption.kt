package mikufan.cx.songfinder.ui.component.mainpage

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.onClick
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.launch
import mikufan.cx.songfinder.backend.controller.mainpage.RegexMatchOptionController
import mikufan.cx.songfinder.backend.statemodel.SearchRegexOption
import mikufan.cx.songfinder.getSpringBean
import mikufan.cx.songfinder.ui.common.RowCentralizedWithSpacing
import mikufan.cx.songfinder.ui.theme.spacing

/**
 * Composable function for handling regex match options.
 *
 */
@Composable
fun RegexMatchOption() {
  val controller = getSpringBean<RegexMatchOptionController>()
  val option by controller.currentRegexOptionState
  RealRegexMatchOption(option, controller::setRegexOption)
}


/**
 * Composable function to render a row of regex match options.
 *
 * @param option The currently selected search regex option.
 * @param onOptionSet The callback function called when a regex option is selected.
 */
@Composable
fun RealRegexMatchOption(option: SearchRegexOption, onOptionSet: suspend (SearchRegexOption) -> Unit) = RowCentralizedWithSpacing(
  horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.spacing)
) {
  Text("Regex Match Option: ")
  RegexMatchOptionButton(SearchRegexOption.Exact, option, onOptionSet)
  RegexMatchOptionButton(SearchRegexOption.StartWith, option, onOptionSet)
  RegexMatchOptionButton(SearchRegexOption.Contains, option, onOptionSet)
}

/**
 * Composable function to render a single regex match option.
 *
 * @param renderedOption The regex option to render.
 * @param selectedOption The currently selected regex option.
 * @param onOptionSet The callback function called when a regex option is selected.
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RegexMatchOptionButton(
  renderedOption: SearchRegexOption,
  selectedOption: SearchRegexOption,
  onOptionSet: suspend (SearchRegexOption) -> Unit
) = Row(
  modifier = Modifier,
  verticalAlignment = Alignment.CenterVertically
) {
  val scope = rememberCoroutineScope()
  RadioButton(
    selected = renderedOption == selectedOption,
    onClick = { scope.launch { onOptionSet(renderedOption) } }
  )
  Text(
    text = renderedOption.displayName,
    modifier = Modifier.onClick { scope.launch { onOptionSet(renderedOption) } }
  )
}
