package com.vacilandia.park.core.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = OceanBlue,
    onPrimary = Color.White,
    primaryContainer = OceanBlueSoft,
    onPrimaryContainer = OceanBlue,
    secondary = Turquoise,
    onSecondary = Color.White,
    secondaryContainer = TurquoiseSoft,
    onSecondaryContainer = Turquoise,
    tertiary = SunYellow,
    onTertiary = OceanDeep,
    tertiaryContainer = SunSoft,
    onTertiaryContainer = SunInk,
    error = StatusDangerText,
    background = SurfaceCanvas,
    onBackground = InkPrimary,
    surface = SurfaceCard,
    onSurface = InkPrimary,
    surfaceVariant = SurfaceSubtle,
    onSurfaceVariant = InkSecondary,
    outline = BorderDefault,
    outlineVariant = BorderStrong
)

@Composable
fun VacilandiaTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = VacilandiaTypography,
        shapes = VacilandiaShapes,
        content = content
    )
}
