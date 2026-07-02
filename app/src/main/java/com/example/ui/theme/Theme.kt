package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SlamDarkColorScheme = darkColorScheme(
    primary = SlamCrimson,
    onPrimary = Color.White,
    primaryContainer = SlamCrimsonDark,
    onPrimaryContainer = Color.White,
    secondary = ElectricLime,
    onSecondary = SlamObsidian,
    secondaryContainer = Color(0xFF2B3A0A),
    onSecondaryContainer = ElectricLime,
    tertiary = ElectricOrange,
    onTertiary = Color.White,
    background = SlamObsidian,
    onBackground = TextPrimary,
    surface = SlamSurface,
    onSurface = TextPrimary,
    surfaceVariant = SlamSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = SlamBorder
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SlamDarkColorScheme,
        typography = Typography,
        content = content
    )
}


