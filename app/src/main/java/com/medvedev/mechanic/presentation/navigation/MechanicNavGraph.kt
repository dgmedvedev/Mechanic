package com.medvedev.mechanic.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsPane
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsScreen
import com.medvedev.mechanic.presentation.cars.edit.CarEditScreen
import com.medvedev.mechanic.presentation.cars.list.CarListScreen
import com.medvedev.mechanic.presentation.docs.NormativeDocsScreen
import com.medvedev.mechanic.presentation.docs.WebViewScreen
import com.medvedev.mechanic.presentation.drivers.detail.DriverDetailsPane
import com.medvedev.mechanic.presentation.drivers.detail.DriverDetailsScreen
import com.medvedev.mechanic.presentation.drivers.edit.DriverEditScreen
import com.medvedev.mechanic.presentation.drivers.list.DriverListScreen
import com.medvedev.mechanic.presentation.cars.fuel.FuelDetailsPane
import com.medvedev.mechanic.presentation.cars.fuel.FuelDetailsScreen
import com.medvedev.mechanic.presentation.cars.fuel.FuelEditScreen
import com.medvedev.mechanic.presentation.cars.fuel.FuelListScreen
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
                onNavigateToFuel = { navController.navigateSingleTop(Routes.FUEL) },
                onNavigateToDocs = { navController.navigateSingleTop(Routes.DOCS) },
            )
        }

        composable(Routes.CARS) {
            var selectedCarId by rememberSaveable { mutableStateOf<String?>(null) }
            var editingCarId by rememberSaveable { mutableStateOf<String?>(null) }

            CarListScreen(
                onBack = { navController.navigateUpOrMain() },
                onNavigateToDetails = { navController.navigate(Routes.carDetails(it)) },
                onNavigateToAdd = { navController.navigate(Routes.CAR_ADD) },
                selectedCarId = selectedCarId,
                onSelectedCarIdChange = { id ->
                    selectedCarId = id
                    if (id != editingCarId) editingCarId = null
                },
                detailPane = { carId ->
                    if (editingCarId == carId) {
                        CarEditScreen(
                            carId = carId,
                            embedded = true,
                            onBack = { editingCarId = null },
                            onSaved = { editingCarId = null },
                        )
                    } else {
                        CarDetailsPane(
                            carId = carId,
                            onNavigateToEdit = { editingCarId = carId },
                            onDeleted = {
                                selectedCarId = null
                                editingCarId = null
                            },
                        )
                    }
                },
            )
        }

        composable(
            route = Routes.CAR_DETAILS,
            arguments = listOf(navArgument("carId") { type = NavType.StringType }),
        ) {
            CarDetailsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Routes.carEdit(it)) },
                onDeleted = { navController.popBackStack() },
            )
        }

        composable(Routes.CAR_ADD) {
            CarEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }

        composable(
            route = Routes.CAR_EDIT,
            arguments = listOf(navArgument("carId") { type = NavType.StringType }),
        ) {
            CarEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }

        composable(Routes.DRIVERS) {
            var selectedDriverId by rememberSaveable { mutableStateOf<String?>(null) }
            var editingDriverId by rememberSaveable { mutableStateOf<String?>(null) }

            DriverListScreen(
                onBack = { navController.navigateUpOrMain() },
                onNavigateToDetails = { navController.navigate(Routes.driverDetails(it)) },
                onNavigateToAdd = { navController.navigate(Routes.DRIVER_ADD) },
                selectedDriverId = selectedDriverId,
                onSelectedDriverIdChange = { id ->
                    selectedDriverId = id
                    if (id != editingDriverId) editingDriverId = null
                },
                detailPane = { driverId ->
                    if (editingDriverId == driverId) {
                        DriverEditScreen(
                            driverId = driverId,
                            embedded = true,
                            onBack = { editingDriverId = null },
                            onSaved = { editingDriverId = null },
                        )
                    } else {
                        DriverDetailsPane(
                            driverId = driverId,
                            onNavigateToEdit = { editingDriverId = driverId },
                            onDeleted = {
                                selectedDriverId = null
                                editingDriverId = null
                            },
                        )
                    }
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

        composable(Routes.FUEL) {
            var selectedCarId by rememberSaveable { mutableStateOf<String?>(null) }
            var editingCarId by rememberSaveable { mutableStateOf<String?>(null) }

            FuelListScreen(
                onBack = { navController.navigateUpOrMain() },
                onNavigateToDetails = { navController.navigate(Routes.fuelDetails(it)) },
                selectedCarId = selectedCarId,
                onSelectedCarIdChange = { id ->
                    selectedCarId = id
                    if (id != editingCarId) editingCarId = null
                },
                detailPane = { carId ->
                    if (editingCarId == carId) {
                        FuelEditScreen(
                            carId = carId,
                            embedded = true,
                            onBack = { editingCarId = null },
                            onSaved = { editingCarId = null },
                        )
                    } else {
                        FuelDetailsPane(
                            carId = carId,
                            onNavigateToEdit = { editingCarId = carId },
                        )
                    }
                },
            )
        }

        composable(
            route = Routes.FUEL_DETAILS,
            arguments = listOf(navArgument("carId") { type = NavType.StringType }),
        ) {
            FuelDetailsScreen(
                onBack = { navController.popBackStack() },
                onNavigateToEdit = { navController.navigate(Routes.fuelEdit(it)) },
            )
        }

        composable(
            route = Routes.FUEL_EDIT,
            arguments = listOf(navArgument("carId") { type = NavType.StringType }),
        ) {
            FuelEditScreen(
                onBack = { navController.popBackStack() },
                onSaved = { navController.popBackStack() },
            )
        }

        composable(Routes.DOCS) {
            NormativeDocsScreen(
                onBack = { navController.navigateUpOrMain() },
                onNavigateToNorms = { navController.navigate(Routes.DOCS_NORMS) },
                onNavigateToResolution470 = { navController.navigate(Routes.DOCS_RESOLUTION470) },
            )
        }

        composable(Routes.DOCS_NORMS) {
            WebViewScreen(
                title = stringResource(R.string.norms),
                url = stringResource(R.string.url_norms),
                onBack = { navController.popBackStack() },
            )
        }

        composable(Routes.DOCS_RESOLUTION470) {
            WebViewScreen(
                title = stringResource(R.string.resolution470),
                url = stringResource(R.string.url_resolution_470),
                onBack = { navController.popBackStack() },
            )
        }
    }
}
