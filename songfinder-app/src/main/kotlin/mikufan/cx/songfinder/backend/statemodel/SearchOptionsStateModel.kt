package mikufan.cx.songfinder.backend.statemodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import org.springframework.stereotype.Component


/**
 * This class represents the state model for search options.
 *
 * @property searchRegexOptionState The state of the search regex option.
 */
@Component
class SearchOptionsStateModel {

  /**
   * Mutable state variable representing the search regex option state.
   *
   * This variable holds the current value of the search regex option state, which determines how the search is performed.
   * It is declared with a `MutableState` type, allowing it to be changed and observed by other parts of the codebase.
   *
   * @property searchRegexOptionState The mutable state object holding the search regex option.
   *
   * @see SearchRegexOption
   *
   */
  val searchRegexOptionState: MutableState<SearchRegexOption> = mutableStateOf(SearchRegexOption.Contains)
}

/**
 * Enum class representing different search options for regular expressions.
 *
 * @property pattern The regular expression pattern for the search option.
 * @property description The description of the search option.
 * @property displayName The formatted display name of the search option.
 */
enum class SearchRegexOption(val pattern: String, val description: String) {
  Exact("^%s$", "Exact Match"),
  StartWith("^%s.*", "Start With"),
  Contains(".*%s.*", "Contains");

  val displayName: String = "$description (${pattern.format("title")})"
}
