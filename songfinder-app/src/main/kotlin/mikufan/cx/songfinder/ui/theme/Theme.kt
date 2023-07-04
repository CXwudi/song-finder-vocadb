package mikufan.cx.songfinder.ui.theme

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import mikufan.cx.songfinder.ui.common.FillInSpacer

// we can override them if we want

private val LightColorPalette = lightColorScheme()

private val DarkColorPalette = darkColorScheme()

private val shapes = Shapes()

private val typography = Typography()

@Composable
fun MyAppTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
  CompositionLocalProvider(
    LocalSpacing provides Spacing(), // so far just use default values
  ) {
    MaterialTheme(
      colorScheme = if (darkTheme) DarkColorPalette else LightColorPalette,
      shapes = shapes,
      typography = typography,
      content = content,
    )
  }
}

/**
 * Apply [MyAppTheme] and the surface to the provided content.
 *
 * @param surfaceModifier The modifier to apply to the surface.
 * By default, it fills the maximum size and sets the background color to the [MaterialTheme]'s background color.
 * @param content The content to be displayed within the surface.
 */
@Composable
fun MyAppThemeWithSurface(
  surfaceModifier: Modifier = Modifier
    .fillMaxSize()
    .background(MaterialTheme.colorScheme.background),
  content: @Composable () -> Unit,
) {
  MyAppTheme {
    Surface(
      modifier = surfaceModifier,
    ) {
      content()
    }
  }
}

@Preview
@Composable
fun previewOfRoot() {
  MyAppThemeWithSurface {
    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      verticalArrangement = Arrangement.SpaceEvenly,
    ) {
      Button(onClick = {}) {
        Text("Click me")
      }
      Text("Hello World")
      FillInSpacer()
    }
  }
}
