package com.example.grameenlight.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = ElectricBlue,
    onPrimary = SurfaceLight,
    primaryContainer = Color(0xFFD6E8FF),
    secondary = SolarYellow,
    onSecondary = OnSurfaceLight,
    background = NeutralLight,
    surface = SurfaceLight,
    error = ErrorRed
)

private val DarkColors = darkColorScheme(
    primary = Color(0xFF90CAFF),
    onPrimary = Color(0xFF003065),
    primaryContainer = ElectricBlueDark,
    secondary = SolarYellow,
    background = NeutralDark,
    surface = SurfaceDark,
    error = Color(0xFFFFB4AB)
)

@Composable
fun GrameenLightTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColors else LightColors
    MaterialTheme(
        colorScheme = colors,
        typography = GrameenTypography,
        content = content
    )
}