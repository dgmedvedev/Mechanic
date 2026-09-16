package com.medvedev.mechanic.presentation.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val TealGradientStart = Color(0xFF018C88)
val TealGradientEnd = Color(0xFF016870)
val TealPrimary = TealGradientStart
val TealDark = Color(0xFF00838F)
val TealPrimaryContainer = Color(0xFFB2DFDB)
val BlueGradientStart = Color(0xFF0D3B66)
val BlueGradientEnd = Color(0xFF051018)
val BlueLight = Color(0xFF3C68A8)
val DarkBackground = Color(0xFF1A1C1E)
val DarkSurface = Color(0xFF2B2D30)
val DarkTextPrimary = Color(0xFFE2E2E6)
val BackgroundGray = Color(0xFFF3F4F6)
val SurfaceVariant = Color(0xFFEEF1F2)
val Outline = Color(0xFFD5DBDE)
val AccentPink = Color(0xFFD81B60)
val PdfHighlight = Color(0xFFFFAA00)
val TextPrimary = Color(0xFF1A1C1E)
val TextSecondary = Color(0xFF6B7280)
val ErrorRed = Color(0xFFC62828)

val TealHeaderGradient = Brush.linearGradient(
    colors = listOf(TealGradientStart, TealGradientEnd),
    start = Offset.Zero,
    end = Offset.Infinite,
)

val BlueHeaderGradient = Brush.linearGradient(
    colors = listOf(BlueGradientStart, BlueGradientEnd),
    start = Offset.Zero,
    end = Offset.Infinite,
)
