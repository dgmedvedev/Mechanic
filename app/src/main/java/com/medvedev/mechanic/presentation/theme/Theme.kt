package com.medvedev.mechanic.presentation.theme

import android.app.Activity
import com.medvedev.mechanic.domain.model.ThemeMode
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

val LocalMechanicHeaderGradient = staticCompositionLocalOf { TealHeaderGradient }
val LocalDarkTheme = staticCompositionLocalOf { false }

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    onPrimary = Color.White,
    primaryContainer = TealPrimaryContainer,
    secondary = TealDark,
    tertiary = AccentPink,
    background = BackgroundGray,
    surface = Color.White,
    onSurface = TextPrimary,
    onSurfaceVariant = TextSecondary,
    surfaceVariant = SurfaceVariant,
    outline = Outline,
    error = ErrorRed,
)

private val DarkColorScheme = darkColorScheme(
    primary = BlueLight,
    onPrimary = Color.White,
    primaryContainer = BlueGradientEnd,
    secondary = BlueGradientStart,
    tertiary = AccentPink,
    background = DarkBackground,
    surface = DarkSurface,
    onSurface = DarkTextPrimary,
    error = ErrorRed,
)

@Composable
fun MechanicTheme(
    themeMode: ThemeMode = ThemeMode.SYSTEM,
    content: @Composable () -> Unit,
) {
    val darkTheme = themeMode.isDark()
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val headerGradient = if (darkTheme) BlueHeaderGradient else TealHeaderGradient
    val view = LocalView.current

    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            val insetsController = WindowCompat.getInsetsController(window, view)
            insetsController.isAppearanceLightStatusBars = false
            insetsController.isAppearanceLightNavigationBars = !darkTheme
        }
    }

    CompositionLocalProvider(
        LocalMechanicHeaderGradient provides headerGradient,
        LocalDarkTheme provides darkTheme,
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            content = content,
        )
    }
}

@Composable
private fun ThemeMode.isDark(): Boolean = when (this) {
    ThemeMode.SYSTEM -> isSystemInDarkTheme()
    ThemeMode.LIGHT -> false
    ThemeMode.DARK -> true
}
