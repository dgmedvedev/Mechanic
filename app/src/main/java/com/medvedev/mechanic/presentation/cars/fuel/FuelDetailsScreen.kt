package com.medvedev.mechanic.presentation.cars.fuel

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.cars.detail.CarDetailViewModel
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.components.MechanicTopBar

@Composable
fun FuelDetailsScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    viewModel: CarDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.notFound) {
        if (uiState.notFound) onBack()
    }

    Scaffold(
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
    onNavigateToEdit: (String) -> Unit,
    viewModel: CarDetailViewModel = hiltViewModel(key = "fuel_detail_$carId"),
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
            onEdit = { onNavigateToEdit(carId) },
        )
    }
}

@Composable
fun FuelDetailsContent(
    car: Car,
    onEdit: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        DetailRow(stringResource(R.string.brand), car.brand)
        DetailRow(stringResource(R.string.model), car.model)
        DetailRow(stringResource(R.string.year_production), car.yearProduction.toString())
        DetailRow(stringResource(R.string.state_number), car.stateNumber)
        DetailRow(stringResource(R.string.linear_fcr), car.linearFuelConsumptionRate)
        DetailRow(
            "${stringResource(R.string.summer_fcr)} ${stringResource(R.string.in_the_city)}",
            car.summerInCityFuelConsumptionRate,
        )
        DetailRow(
            "${stringResource(R.string.summer_fcr)} ${stringResource(R.string.outside_the_city)}",
            car.summerOutCityFuelConsumptionRate,
        )
        DetailRow(
            "${stringResource(R.string.winter_fcr)} ${stringResource(R.string.in_the_city)}",
            car.winterInCityFuelConsumptionRate,
        )
        DetailRow(
            "${stringResource(R.string.winter_fcr)} ${stringResource(R.string.outside_the_city)}",
            car.winterOutCityFuelConsumptionRate,
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
        ) {
            Button(onClick = onEdit, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(R.string.edit))
            }
        }
    }
}
