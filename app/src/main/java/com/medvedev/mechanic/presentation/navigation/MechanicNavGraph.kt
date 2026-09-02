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
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.medvedev.mechanic.presentation.cars.CarDetailSection
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsPane
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsScreen
import com.medvedev.mechanic.presentation.cars.edit.CarEditScreen
import com.medvedev.mechanic.presentation.cars.list.CarListScreen
import com.medvedev.mechanic.presentation.docs.NormativeDocsScreen
import com.medvedev.mechanic.presentation.docs.PdfDocumentScreen
import com.medvedev.mechanic.presentation.drivers.detail.DriverDetailsPane
import com.medvedev.mechanic.presentation.drivers.detail.DriverDetailsScreen
import com.medvedev.mechanic.presentation.drivers.edit.DriverEditScreen
import com.medvedev.mechanic.presentation.drivers.list.DriverListScreen
import com.medvedev.mechanic.presentation.main.MainMenuScreen

fun NavHostController.navigateSingleTop(route: String) {
    navigate(route) {
        launchSingleTop = true
    }
}

fun NavHostController.navigateUpOrMain() {
    if (!popBackStack()) {
        navigateSingleTop(Routes.MAIN)
    }
}

@Composable
fun MechanicNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.MAIN,
        modifier = modifier,
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None },
    ) {
        composable(Routes.MAIN) {
            MainMenuScreen(
                onNavigateToCars = { navController.navigateSingleTop(Routes.CARS) },
                onNavigateToDrivers = { navController.navigateSingleTop(Routes.DRIVERS) },
                onNavigateToDocs = { navController.navigateSingleTop(Routes.DOCS) },
            )
        }

        composable(Routes.CARS) {
            var section by rememberSaveable { mutableStateOf(CarDetailSection.DATA) }
            CarListScreen(
                onBack = { navController.navigateUpOrMain() },
                onNavigateToDetails = { navController.navigate(Routes.carDetails(it)) },
                onNavigateToAdd = { navController.navigate(Routes.CAR_ADD) },
                detailContent = { carId, onEdit, onDeleted ->
                    CarDetailsPane(
                        carId = carId,
                        section = section,
                        onSectionChange = { section = it },
                        onNavigateToEdit = onEdit,
                        onDeleted = onDeleted,
                    )
                },
                editContent = { carId, onClose ->
                    CarEditScreen(
                        carId = carId,
                        embedded = true,
                        section = section,
                        onSectionChange = { section = it },
                        onBack = onClose,
                        onSaved = onClose,
                    )
                },
            )
        }

        composable(
            route = Routes.CAR_DETAILS,
            arguments = listOf(navArgument("carId") { type = NavType.StringType }),
        ) {
            CarDetailsScreen(
                onBack = { navController.popBackStack() },
                onDeleted = { navController.popBackStack() },
            )
        }

        composable(Routes.CAR_ADD) {
            var section by rememberSaveable { mutableStateOf(CarDetailSection.DATA) }
            CarEditScreen(
                section = section,
                onSectionChange = { section = it },
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }

        composable(Routes.DRIVERS) {
            DriverListScreen(
                onBack = { navController.navigateUpOrMain() },
                onNavigateToDetails = { navController.navigate(Routes.driverDetails(it)) },
                onNavigateToAdd = { navController.navigate(Routes.DRIVER_ADD) },
                detailContent = { driverId, onEdit, onDeleted ->
                    DriverDetailsPane(
                        driverId = driverId,
                        onNavigateToEdit = onEdit,
                        onDeleted = onDeleted,
                    )
                },
                editContent = { driverId, onClose ->
                    DriverEditScreen(
                        driverId = driverId,
                        embedded = true,
                        onBack = onClose,
                        onSaved = onClose,
                    )
                },
            )
        }

        composable(
            route = Routes.DRIVER_DETAILS,
            arguments = listOf(navArgument("driverId") { type = NavType.StringType }),
        ) {
            DriverDetailsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Routes.driverEdit(it)) },
                onDeleted = { navController.popBackStack() },
            )
        }

        composable(Routes.DRIVER_ADD) {
            DriverEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.DRIVER_EDIT,
            arguments = listOf(navArgument("driverId") { type = NavType.StringType }),
        ) {
            DriverEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }

        composable(Routes.DOCS) {
            NormativeDocsScreen(
                onBack = { navController.navigateUpOrMain() },
                onNavigateToDocument = { navController.navigate(Routes.docView(it)) },
            )
        }

        composable(
            route = Routes.DOC_VIEW,
            arguments = listOf(navArgument("documentId") { type = NavType.StringType }),
        ) {
            PdfDocumentScreen(
                onBack = { navController.popBackStack() },
            )
        }
    }
}
