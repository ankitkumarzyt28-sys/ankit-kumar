package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val ToolVerseDarkColorScheme = darkColorScheme(
    primary = CyanPrimary,
    onPrimary = Color(0xFF001F29),
    primaryContainer = Color(0xFF004D5A),
    onPrimaryContainer = CyanPrimaryLight,

    secondary = VioletSecondaryLight,
    onSecondary = Color(0xFF2E004E),
    secondaryContainer = Color(0xFF4A1078),
    onSecondaryContainer = Color(0xFFEADBFF),

    tertiary = IndigoAccent,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFF25399E),
    onTertiaryContainer = Color(0xFFDDE1FF),

    background = DarkBackground,
    onBackground = DarkTextPrimary,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkCardBorder,
    outlineVariant = Color(0xFF1E293B)
)

val ToolVerseLightColorScheme = lightColorScheme(
    primary = Color(0xFF006677),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBCEBFF),
    onPrimaryContainer = Color(0xFF001F26),

    secondary = VioletSecondary,
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEADBFF),
    onSecondaryContainer = Color(0xFF2E004E),

    tertiary = IndigoAccent,
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFDDE1FF),
    onTertiaryContainer = Color(0xFF0B195C),

    background = LightBackground,
    onBackground = LightTextPrimary,
    surface = LightSurface,
    onSurface = LightTextPrimary,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightTextSecondary,
    outline = LightCardBorder,
    outlineVariant = Color(0xFFCBD5E1)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) ToolVerseDarkColorScheme else ToolVerseLightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
