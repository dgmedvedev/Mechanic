package com.medvedev.mechanic.presentation.navigation

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
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
            var selectedCarId by remember { mutableStateOf<String?>(null) }
            BoxWithConstraints {
                val isExpanded = maxWidth >= 600.dp
                CarListScreen(
                    onBack = { navController.navigateUpOrMain() },
                    onNavigateToDetails = { carId ->
                        if (isExpanded) {
                            selectedCarId = carId
                        } else {
                            navController.navigate(Routes.carDetails(carId))
                        }
                    },
                    onNavigateToAdd = { navController.navigate(Routes.CAR_ADD) },
                    selectedCarId = if (isExpanded) selectedCarId else null,
                    detailContent = if (isExpanded && selectedCarId != null) {
                        {
                            CarDetailsPane(
                                carId = selectedCarId!!,
                                onNavigateToEdit = { navController.navigate(Routes.carEdit(it)) },
                                onDeleted = { selectedCarId = null },
                            )
                        }
                    } else {
                        null
                    },
                )
            }
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
            var selectedDriverId by remember { mutableStateOf<String?>(null) }
            BoxWithConstraints {
                val isExpanded = maxWidth >= 600.dp
                DriverListScreen(
                    onBack = { navController.navigateUpOrMain() },
                    onNavigateToDetails = { driverId ->
                        if (isExpanded) {
                            selectedDriverId = driverId
                        } else {
                            navController.navigate(Routes.driverDetails(driverId))
                        }
                    },
                    onNavigateToAdd = { navController.navigate(Routes.DRIVER_ADD) },
                    selectedDriverId = if (isExpanded) selectedDriverId else null,
                    detailContent = if (isExpanded && selectedDriverId != null) {
                        {
                            DriverDetailsPane(
                                driverId = selectedDriverId!!,
                                onNavigateToEdit = { navController.navigate(Routes.driverEdit(it)) },
                                onDeleted = { selectedDriverId = null },
                            )
                        }
                    } else {
                        null
                    },
                )
            }
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
            var selectedCarId by remember { mutableStateOf<String?>(null) }
            BoxWithConstraints {
                val isExpanded = maxWidth >= 600.dp
                FuelListScreen(
                    onBack = { navController.navigateUpOrMain() },
                    onNavigateToDetails = { carId ->
                        if (isExpanded) {
                            selectedCarId = carId
                        } else {
                            navController.navigate(Routes.fuelDetails(carId))
                        }
                    },
                    selectedCarId = if (isExpanded) selectedCarId else null,
                    detailContent = if (isExpanded && selectedCarId != null) {
                        {
                            FuelDetailsPane(
                                carId = selectedCarId!!,
                                onNavigateToEdit = { navController.navigate(Routes.fuelEdit(it)) },
                            )
                        }
                    } else {
                        null
                    },
                )
            }
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
