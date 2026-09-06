package com.medvedev.mechanic.presentation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.medvedev.mechanic.presentation.navigation.MechanicNavGraph
import com.medvedev.mechanic.presentation.navigation.MechanicNavigationBar
import com.medvedev.mechanic.presentation.navigation.Routes
import com.medvedev.mechanic.presentation.navigation.navigateToTab
import com.medvedev.mechanic.presentation.theme.MechanicTheme

@Composable
fun MechanicApp() {
    MechanicTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val showBottomBar = currentRoute in Routes.topLevel

        Scaffold(
            contentWindowInsets = WindowInsets(0, 0, 0, 0),
            bottomBar = {
                if (showBottomBar) {
                    MechanicNavigationBar(
                        currentRoute = currentRoute,
                        onTabClick = navController::navigateToTab,
                    )
                }
            },
        ) { padding ->
            MechanicNavGraph(
                navController = navController,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            )
        }
    }
}
