package mikufan.cx.songfinder.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Shapes
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val LightColorPalette = lightColors(
  primary = Color(0xFF6200EE),
  primaryVariant = Color(0xFF3700B3),
  secondary = Color(0xFF03DAC6),
  background = Color.White,
  surface = Color.White,
  onPrimary = Color.White,
  onSecondary = Color.Black,
  onBackground = Color.Black,
  onSurface = Color.Black,
)

private val DarkColorPalette = darkColors(
  primary = Color(0xFFBB86FC),
  primaryVariant = Color(0xFF3700B3),
  secondary = Color(0xFF03DAC6),
  background = Color(0xFF121212),
  surface = Color(0xFF121212),
  onPrimary = Color.Black,
  onSecondary = Color.Black,
  onBackground = Color.White,
  onSurface = Color.White,
)

private val shapes = Shapes(
  small = RoundedCornerShape(8.dp),
  medium = RoundedCornerShape(12.dp),
  large = CutCornerShape(topStart = 16.dp, bottomEnd = 16.dp),
)

@Composable
fun MyAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  MaterialTheme(
    colors = if (darkTheme) DarkColorPalette else LightColorPalette,
    shapes = shapes,
    content = content,
  )
}
