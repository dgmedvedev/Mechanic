package com.medvedev.mechanic.presentation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.medvedev.mechanic.presentation.components.ExpandedListDetailBreakpoint
import com.medvedev.mechanic.presentation.navigation.LocalTopLevelNav
import com.medvedev.mechanic.presentation.navigation.MechanicNavGraph
import com.medvedev.mechanic.presentation.navigation.TopLevelNav
import com.medvedev.mechanic.presentation.navigation.navigateToTab
import com.medvedev.mechanic.presentation.theme.MechanicTheme

@Composable
fun MechanicApp() {
    MechanicTheme {
        val navController = rememberNavController()
        val navBackStackEntry by navController.currentBackStackEntryAsState()

        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            CompositionLocalProvider(
                LocalTopLevelNav provides TopLevelNav(
                    currentRoute = navBackStackEntry?.destination?.route,
                    onTabClick = navController::navigateToTab,
                    wideLayout = maxWidth >= ExpandedListDetailBreakpoint,
                ),
            ) {
                MechanicNavGraph(
                    navController = navController,
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
