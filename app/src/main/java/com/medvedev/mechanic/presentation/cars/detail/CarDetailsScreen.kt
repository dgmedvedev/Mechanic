package com.medvedev.mechanic.presentation.cars.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AcUnit
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.CarRepair
import androidx.compose.material.icons.outlined.DirectionsCar
import androidx.compose.material.icons.outlined.HealthAndSafety
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Pin
import androidx.compose.material.icons.outlined.Scale
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.medvedev.mechanic.presentation.cars.CarDetailSection
import com.medvedev.mechanic.presentation.cars.CarDetailSectionSelector
import com.medvedev.mechanic.presentation.cars.edit.CarEditScreen
import com.medvedev.mechanic.presentation.components.DetailContentLayout
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.components.MechanicTopBar
import com.medvedev.mechanic.presentation.preview.PreviewCar
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun CarDetailsScreen(
    onBack: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: CarDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val section = rememberSaveable { mutableStateOf(CarDetailSection.DATA) }
    val editing = rememberSaveable { mutableStateOf(false) }
    val car = uiState.item

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
                title = stringResource(R.string.menu_button1),
                onBack = {
                    if (editing.value) {
                        editing.value = false
                    } else {
                        onBack()
                    }
                },
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                editing.value && car != null -> CarEditScreen(
                    carId = car.id,
                    embedded = true,
                    section = section.value,
                    onSectionChange = { section.value = it },
                    onBack = { editing.value = false },
                    onSaved = {
                        editing.value = false
                        viewModel.refresh()
                    },
                )

                uiState.isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))

                car != null -> CarDetailsContent(
                    car = car,
                    section = section.value,
                    onSectionChange = { section.value = it },
                    onEditClick = { editing.value = true },
                    onDeleteClick = { viewModel.deleteCar(onDeleted) },
                )
            }
        }
    }
}

@Composable
fun CarDetailsPane(
    carId: String,
    section: CarDetailSection,
    onSectionChange: (CarDetailSection) -> Unit,
    onNavigateToEdit: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: CarDetailsViewModel = hiltViewModel(key = "car_detail_$carId"),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val car = uiState.item

    LaunchedEffect(carId) {
        viewModel.loadCar(carId)
    }

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        car != null -> CarDetailsContent(
            car = car,
            section = section,
            onSectionChange = onSectionChange,
            onEditClick = onNavigateToEdit,
            onDeleteClick = { viewModel.deleteCar(onDeleted) },
        )
    }
}

@Composable
fun CarDetailsContent(
    car: Car,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    section: CarDetailSection = CarDetailSection.DATA,
    onSectionChange: (CarDetailSection) -> Unit = {},
) {
    DetailContentLayout(
        onEditClick = onEditClick,
        onDeleteClick = onDeleteClick,
    ) {
        CarIdentityRows(car)
        CarDetailSectionSelector(
            selected = section,
            onSelected = onSectionChange,
        )
        when (section) {
            CarDetailSection.DATA -> CarDataRows(car)
            CarDetailSection.FUEL_RATES -> CarFuelRateRows(car)
        }
    }
}

@Composable
private fun CarIdentityRows(car: Car) {
    DetailRow(stringResource(R.string.brand), car.brand, icon = Icons.Outlined.DirectionsCar)
    DetailRow(stringResource(R.string.model), car.model, icon = Icons.Outlined.DirectionsCar)
    DetailRow(
        stringResource(R.string.year_production),
        car.yearProduction.toString(),
        icon = Icons.Outlined.CalendarMonth,
    )
    DetailRow(stringResource(R.string.state_number), car.stateNumber, icon = Icons.Outlined.Pin)
}

@Composable
private fun CarDataRows(car: Car) {
    DetailRow(stringResource(R.string.vin), car.vin, icon = Icons.Outlined.Pin)
    DetailRow(
        stringResource(R.string.engine_displacement),
        car.engineDisplacement,
        icon = Icons.Outlined.Speed,
    )
    DetailRow(
        stringResource(R.string.fuel_type),
        car.fuelType,
        icon = Icons.Outlined.LocalGasStation,
    )
    DetailRow(
        stringResource(R.string.allowable_weight),
        car.allowableWeight,
        icon = Icons.Outlined.Scale,
    )
    DetailRow(
        stringResource(R.string.technical_passport),
        car.technicalPassport,
        icon = Icons.Outlined.Badge,
    )
    DetailRow(stringResource(R.string.checkup), car.checkup, icon = Icons.Outlined.CarRepair)
    DetailRow(stringResource(R.string.insurance), car.insurance, icon = Icons.Outlined.Shield)
    DetailRow(
        stringResource(R.string.hull_insurance),
        car.hullInsurance,
        icon = Icons.Outlined.HealthAndSafety,
    )
}

@Composable
private fun CarFuelRateRows(car: Car) {
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

@PreviewLightDark
@Composable
private fun CarDetailsContentPreview() {
    PreviewMechanicTheme {
        CarDetailsContent(
            car = PreviewCar,
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
