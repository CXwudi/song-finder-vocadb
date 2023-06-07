package mikufan.cx.songfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

// we can override them if we want

private val LightColorPalette = lightColorScheme()

private val DarkColorPalette = darkColorScheme()

private val shapes = Shapes()

private val typography = Typography()

@Composable
fun MyAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  MaterialTheme(
    colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
    shapes = shapes,
    typography = typography,
    content = content,
  )
}
