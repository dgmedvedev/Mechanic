package com.medvedev.mechanic.presentation.cars.fuel

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.presentation.cars.detail.CarDetailsViewModel
import com.medvedev.mechanic.presentation.components.DetailContentLayout
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.components.MechanicTopBar
import com.medvedev.mechanic.presentation.preview.PreviewCar
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun FuelDetailsScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: CarDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) {
        viewModel.refresh()
    }

    LaunchedEffect(uiState.notFound) {
        if (uiState.notFound) onBack()
    }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.menu_button3),
                onBack = onBack,
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                uiState.item != null -> FuelDetailsContent(
                    car = uiState.item!!,
                    onEdit = { onNavigateToEdit(uiState.item!!.id) },
                )
            }
        }
    }
}

@Composable
fun FuelDetailsPane(
    carId: String,
    onNavigateToEdit: () -> Unit,
    viewModel: CarDetailsViewModel = hiltViewModel(key = "fuel_detail_$carId"),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(carId) {
        viewModel.loadCar(carId)
    }

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        uiState.item != null -> FuelDetailsContent(
            car = uiState.item!!,
            onEdit = onNavigateToEdit,
        )
    }
}

@Composable
fun FuelDetailsContent(
    car: Car,
    onEdit: () -> Unit,
) {
    DetailContentLayout(
        onEditClick = onEdit,
        showDelete = false,
    ) {
        DetailRow(stringResource(R.string.brand), car.brand, icon = Icons.Outlined.DirectionsCar)
        DetailRow(stringResource(R.string.model), car.model, icon = Icons.Outlined.DirectionsCar)
        DetailRow(
            stringResource(R.string.year_production),
            car.yearProduction.toString(),
            icon = Icons.Outlined.CalendarMonth,
        )
        DetailRow(stringResource(R.string.state_number), car.stateNumber, icon = Icons.Outlined.Pin)
        DetailRow(
            stringResource(R.string.linear_fcr),
            car.linearFuelConsumptionRate,
            icon = Icons.Outlined.Speed,
        )
        DetailRow(
            "${stringResource(R.string.summer_fcr)}\n${stringResource(R.string.in_the_city)}",
            car.summerInCityFuelConsumptionRate,
            icon = Icons.Outlined.WbSunny,
        )
        DetailRow(
            "${stringResource(R.string.summer_fcr)}\n${stringResource(R.string.outside_the_city)}",
            car.summerOutCityFuelConsumptionRate,
            icon = Icons.Outlined.WbSunny,
        )
        DetailRow(
            "${stringResource(R.string.winter_fcr)}\n${stringResource(R.string.in_the_city)}",
            car.winterInCityFuelConsumptionRate,
            icon = Icons.Outlined.AcUnit,
        )
        DetailRow(
            "${stringResource(R.string.winter_fcr)}\n${stringResource(R.string.outside_the_city)}",
            car.winterOutCityFuelConsumptionRate,
            icon = Icons.Outlined.AcUnit,
        )
    }
}

@PreviewLightDark
@Composable
private fun FuelDetailsContentPreview() {
    PreviewMechanicTheme {
        FuelDetailsContent(
            car = PreviewCar,
            onEdit = {},
        )
    }
}
