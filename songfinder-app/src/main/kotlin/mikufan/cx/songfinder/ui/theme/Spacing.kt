package mikufan.cx.songfinder.ui.theme

import androidx.compose.runtime.ProvidableCompositionLocal
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * @author CX无敌
 * 2023-06-14
 */

val LocalSpacing: ProvidableCompositionLocal<Spacing>
  get() = staticCompositionLocalOf { Spacing() }

data class Spacing(
  val spacing: Dp = DefaultSpacing,
  val padding: Dp = DefaultSpacing,
)

private val DefaultSpacing = 5.dp
