package com.medvedev.presentation.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = LightBlue,
    secondary = TealDark,
    tertiary = AccentPink,
    background = BackgroundGray,
    surface = Color.White,
    onSurface = TealDark,
)

private val DarkColorScheme = darkColorScheme(
    primary = LightBlue,
    onPrimary = Color.White,
    primaryContainer = TealDark,
    secondary = TealPrimary,
    tertiary = AccentPink,
    background = Color(0xFF1A1C1E),
    surface = Color(0xFF2B2D30),
    onSurface = Color(0xFFE2E2E6),
)

@Composable
fun MechanicTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content,
    )
}
