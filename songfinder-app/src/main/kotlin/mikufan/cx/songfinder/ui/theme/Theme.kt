package mikufan.cx.songfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorPalette = lightColors(
  primary = Color.Blue,
  primaryVariant = Color.Blue,
  secondary = Color.Blue,
  background = Color.White,
  surface = Color.White,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onBackground = Color.Black,
  onSurface = Color.Black,
)
private val DarkColorPalette = darkColors(
  primary = Color.Blue,
  primaryVariant = Color.Blue,
  secondary = Color.Blue,
  background = Color.Black,
  surface = Color.Black,
  onPrimary = Color.White,
  onSecondary = Color.White,
  onBackground = Color.White,
  onSurface = Color.White,
)

@Composable
fun MyAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  MaterialTheme(
    colors = if (darkTheme) DarkColorPalette else LightColorPalette,
    content = content,
  )
}
