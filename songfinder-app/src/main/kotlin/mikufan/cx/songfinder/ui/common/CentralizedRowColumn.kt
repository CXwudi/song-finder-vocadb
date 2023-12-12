package mikufan.cx.songfinder.ui.common

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.ui.theme.spacing

/**
 * A row layout with centralized content and added spacing.
 *
 * @param modifier The modifier to apply to the row layout.
 * @param furtherModifier The further modifier to apply to the row layout.
 * @param horizontalArrangement The horizontal arrangement of items in the row layout.
 * @param verticalAlignment The vertical alignment of items in the row layout.
 * @param content The content to be displayed in the row layout, provided as a composable function.
 */
@Composable
fun RowCentralizedWithSpacing(
  modifier: Modifier = Modifier.padding(horizontal = MaterialTheme.spacing.padding),
  furtherModifier: Modifier = Modifier,
  horizontalArrangement: Arrangement.Horizontal = Arrangement.spacedBy(MaterialTheme.spacing.spacing),
  verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
  content: @Composable RowScope.() -> Unit
) {
  Row(
    modifier.then(furtherModifier),
    horizontalArrangement = horizontalArrangement,
    verticalAlignment = verticalAlignment,
  ) {
    content()
  }
}

/**
 * A column layout with added spacing.
 *
 * @param modifier The modifier to be applied to the column layout.
 * @param furtherModifier The further modifier to be applied to the column layout.
 * @param verticalArrangement The vertical arrangement for the column layout.
 * @param horizontalAlignment The horizontal alignment for the column layout.
 * @param content The content of the column layout.
 */
@Composable
fun ColumnWithSpacing(
  modifier: Modifier = Modifier.padding(vertical = MaterialTheme.spacing.padding),
  furtherModifier: Modifier = Modifier,
  verticalArrangement: Arrangement.Vertical = Arrangement.spacedBy(MaterialTheme.spacing.spacing),
  horizontalAlignment: Alignment.Horizontal = Alignment.Start,
  content: @Composable ColumnScope.() -> Unit
) {
  Column(
    modifier.then(furtherModifier),
    verticalArrangement = verticalArrangement,
    horizontalAlignment = horizontalAlignment,
  ) {
    content()
  }
}

