package ui.theme
package pe.edu.epis.alquicompra.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    secondary = PrimaryLight,
    tertiary = Accent,
    background = Background,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = TextDark,
    onBackground = TextDark,
    onSurface = TextDark,
)

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryLight,
    secondary = Primary,
    tertiary = Accent,
    background = Gray900,
    surface = Gray800,
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = TextDark,
    onBackground = Color.White,
    onSurface = Color.White,
)

@Composable
fun AlquiCompraTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        DarkColorScheme
    } else {
        LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}