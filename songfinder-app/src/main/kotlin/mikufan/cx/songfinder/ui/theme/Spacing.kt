package mikufan.cx.songfinder.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author CX无敌
 * 2023-06-14
 */

val MaterialTheme.spacing: Spacing
  @Composable
  @ReadOnlyComposable
  get() = LocalSpacing.current

internal val LocalSpacing: ProvidableCompositionLocal<Spacing> = staticCompositionLocalOf {
  error("No Spacing provided")
}

data class Spacing(
  val spacing: Dp = DefaultSpacing,
  val padding: Dp = DefaultSpacing,
)

private val DefaultSpacing = 5.dp
