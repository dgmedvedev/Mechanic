package com.medvedev.mechanic.presentation.navigation

import androidx.compose.runtime.compositionLocalOf

data class AppActions(
    val openAbout: () -> Unit = {},
    val openSettings: () -> Unit = {},
)

val LocalAppActions = compositionLocalOf<AppActions?> { null }
