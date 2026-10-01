package com.example.ui.theme

import android.os.Build
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val BlackGoldEmeraldColorScheme = darkColorScheme(
    primary = GoldenButtonPrimary,
    onPrimary = GoldenButtonContent,
    primaryContainer = EmeraldFoundationHeader,
    onPrimaryContainer = Color.White,
    secondary = GoldenButtonPrimary,
    onSecondary = Color.Black,
    secondaryContainer = GoldenBadgeBg,
    onSecondaryContainer = GoldenButtonLight,
    background = DarkAppBg,
    surface = DarkAppSurface,
    onBackground = Color.White,
    onSurface = Color.White,
    surfaceVariant = DarkAppSurfaceElevated,
    onSurfaceVariant = DarkAppTextSecondary,
    outline = DarkAppBorder,
    error = RoseAlert,
    errorContainer = RoseLight
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Default to true as user requested black theme inside the app
    dynamicColor: Boolean = false, // Keep false to preserve AL-KHEDMAH black & gold & green theme
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = BlackGoldEmeraldColorScheme,
        typography = Typography,
        content = content
    )
}
