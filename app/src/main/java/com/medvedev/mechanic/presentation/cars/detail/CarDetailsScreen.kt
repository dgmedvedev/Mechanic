package com.medvedev.mechanic.presentation.cars.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
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
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.components.MechanicTopBar

@Composable
fun CarDetailsScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onDeleted: () -> Unit,
    viewModel: CarDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.notFound) {
        if (uiState.notFound) onBack()
    }

    Scaffold(
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.menu_button1),
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
                uiState.item != null -> CarDetailsContent(
                    car = uiState.item!!,
                    onEdit = { onNavigateToEdit(uiState.item!!.id) },
                    onDelete = { viewModel.deleteCar(onDeleted) },
                )
            }
        }
    }
}

@Composable
fun CarDetailsPane(
    carId: String,
    onNavigateToEdit: (String) -> Unit,
    onDeleted: () -> Unit,
    viewModel: CarDetailViewModel = hiltViewModel(key = "car_detail_$carId"),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(carId) {
        viewModel.loadCar(carId)
    }

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        uiState.item != null -> CarDetailsContent(
            car = uiState.item!!,
            onEdit = { onNavigateToEdit(carId) },
            onDelete = { viewModel.deleteCar(onDeleted) },
        )
    }
}

@Composable
fun CarDetailsContent(
    car: Car,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        DetailRow(stringResource(R.string.brand), car.brand)
        DetailRow(stringResource(R.string.model), car.model)
        DetailRow(stringResource(R.string.year_production), car.yearProduction.toString())
        DetailRow(stringResource(R.string.state_number), car.stateNumber)
        DetailRow(stringResource(R.string.body_number), car.bodyNumber)
        DetailRow(stringResource(R.string.engine_displacement), car.engineDisplacement)
        DetailRow(stringResource(R.string.fuel_type), car.fuelType)
        DetailRow(stringResource(R.string.allowable_weight), car.allowableWeight)
        DetailRow(stringResource(R.string.technical_passport), car.technicalPassport)
        DetailRow(stringResource(R.string.checkup), car.checkup)
        DetailRow(stringResource(R.string.insurance), car.insurance)
        DetailRow(stringResource(R.string.hull_insurance), car.hullInsurance)
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Button(onClick = onEdit, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.edit))
            }
            OutlinedButton(onClick = onDelete, modifier = Modifier.weight(1f)) {
                Text(stringResource(R.string.delete))
            }
        }
    }
}
