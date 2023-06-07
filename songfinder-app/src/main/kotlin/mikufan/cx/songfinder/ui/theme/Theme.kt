package mikufan.cx.songfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

// we can override them if we want

private val LightColorPalette = lightColorScheme()

private val DarkColorPalette = darkColorScheme()

private val shapes = Shapes()

@Composable
fun MyAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
    shapes = shapes,
    content = content,
  )
}
