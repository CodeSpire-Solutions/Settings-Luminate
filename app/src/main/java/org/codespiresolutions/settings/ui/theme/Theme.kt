// Theme.kt
package org.codespiresolutions.settings.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = Color(0xFF6200EE),
    onPrimary = Color.White,
    background = LightBackgroundColor,
    onBackground = LightPrimaryTextColor,
    surface = Color.White,
    onSurface = LightPrimaryTextColor,
    surfaceVariant = Color(0xFFEBEBEB),
    onSurfaceVariant = LightPrimaryTextColor
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFFBB86FC),
    onPrimary = Color.Black,
    background = DarkBackgroundColor,
    onBackground = DarkPrimaryTextColor,
    surface = Color(0xFF121212),
    onSurface = DarkPrimaryTextColor,
    surfaceVariant = Color(0xFF333333),
    onSurfaceVariant = DarkSecondaryTextColor
)

@Composable
fun SettingsTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}