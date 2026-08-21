package com.medvedev.mechanic.presentation.theme

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

val TealGradientStart = Color(0xFF018C88)
val TealGradientEnd = Color(0xFF016870)
val TealPrimary = TealGradientStart
val TealDark = Color(0xFF00838F)
val TealPrimaryContainer = Color(0xFFB2DFDB)
val LightBlue = Color(0xFF4FA1BB)
val LightGreen = Color(0xFF13EB36)
val BackgroundGray = Color(0xFFF3F4F6)
val SurfaceVariant = Color(0xFFEEF1F2)
val Outline = Color(0xFFD5DBDE)
val AccentPink = Color(0xFFD81B60)
val TextPrimary = Color(0xFF1A1C1E)
val TextSecondary = Color(0xFF6B7280)
val ErrorRed = Color(0xFFC62828)

val MechanicHeaderGradient = Brush.linearGradient(
    colors = listOf(TealGradientStart, TealGradientEnd),
    start = Offset.Zero,
    end = Offset.Infinite,
)
