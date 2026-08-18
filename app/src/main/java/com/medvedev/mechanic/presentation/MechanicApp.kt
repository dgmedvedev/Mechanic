package com.medvedev.mechanic.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.medvedev.mechanic.presentation.navigation.MechanicNavGraph
import com.medvedev.mechanic.presentation.theme.MechanicTheme

@Composable
fun MechanicApp() {
    MechanicTheme {
        val navController = rememberNavController()
        MechanicNavGraph(
            navController = navController,
            modifier = Modifier.fillMaxSize(),
        )
    }
}
