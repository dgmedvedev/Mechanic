package com.medvedev.mechanic.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.medvedev.mechanic.presentation.cars.CarDetailSection
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsPane
import com.medvedev.mechanic.presentation.cars.edit.CarEditPane
import com.medvedev.mechanic.presentation.cars.list.CarListScreen
import com.medvedev.mechanic.presentation.docs.DocsListScreen
import com.medvedev.mechanic.presentation.docs.PdfDocumentPane
import com.medvedev.mechanic.presentation.drivers.detail.DriverDetailsPane
import com.medvedev.mechanic.presentation.drivers.edit.DriverEditPane
import com.medvedev.mechanic.presentation.drivers.list.DriverListScreen

fun NavHostController.navigateToTab(route: String) {
    if (currentDestination?.route == route) return
    navigate(route) {
        popUpTo(graph.id) {
            saveState = true
        }
        launchSingleTop = true
        restoreState = true
    }
}

@Composable
fun MechanicNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.CARS,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(Routes.CARS) {
            var section by rememberSaveable { mutableStateOf(CarDetailSection.DATA) }
            CarListScreen(
                detailContent = { carId, onEdit, onDeleted ->
                    CarDetailsPane(
                        carId = carId,
                        section = section,
                        onSectionChange = { section = it },
                        onNavigateToEdit = onEdit,
                        onDeleted = onDeleted,
                    )
                },
                editContent = { carId, embedded, onClose ->
                    CarEditPane(
                        carId = carId,
                        embedded = embedded,
                        section = section,
                        onSectionChange = { section = it },
                        onBack = onClose,
                        onSaved = onClose,
                    )
                },
            )
        }

        composable(Routes.DRIVERS) {
            DriverListScreen(
                detailContent = { driverId, onEdit, onDeleted ->
                    DriverDetailsPane(
                        driverId = driverId,
                        onNavigateToEdit = onEdit,
                        onDeleted = onDeleted,
                    )
                },
                editContent = { driverId, embedded, onClose ->
                    DriverEditPane(
                        driverId = driverId,
                        embedded = embedded,
                        onBack = onClose,
                        onSaved = onClose,
                    )
                },
            )
        }

        composable(Routes.DOCS) {
            DocsListScreen(
                documentContent = { documentId, embedded, onClose ->
                    PdfDocumentPane(
                        documentId = documentId,
                        embedded = embedded,
                        onBack = onClose,
                    )
                },
            )
        }
    }
}
