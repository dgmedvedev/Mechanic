package com.medvedev.mechanic.presentation.drivers.detail

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
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.components.MechanicTopBar

@Composable
fun DriverDetailsScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onDeleted: () -> Unit,
    viewModel: DriverDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.notFound) {
        if (uiState.notFound) onBack()
    }

    Scaffold(
        topBar = {
            MechanicTopBar(
                title = stringResource(R.string.menu_button2),
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
                uiState.item != null -> DriverDetailsContent(
                    driver = uiState.item!!,
                    onEdit = { onNavigateToEdit(uiState.item!!.id) },
                    onDelete = { viewModel.deleteDriver(onDeleted) },
                )
            }
        }
    }
}

@Composable
fun DriverDetailsPane(
    driverId: String,
    onNavigateToEdit: (String) -> Unit,
    onDeleted: () -> Unit,
    viewModel: DriverDetailViewModel = hiltViewModel(key = "driver_detail_$driverId"),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(driverId) {
        viewModel.loadDriver(driverId)
    }

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        uiState.item != null -> DriverDetailsContent(
            driver = uiState.item!!,
            onEdit = { onNavigateToEdit(driverId) },
            onDelete = { viewModel.deleteDriver(onDeleted) },
        )
    }
}

@Composable
fun DriverDetailsContent(
    driver: Driver,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        DetailRow(stringResource(R.string.surname), driver.surname)
        DetailRow(stringResource(R.string.name), driver.name)
        DetailRow(stringResource(R.string.middle_name), driver.middleName)
        DetailRow(stringResource(R.string.birthday), driver.birthday)
        DetailRow(stringResource(R.string.driving_license_number), driver.drivingLicenseNumber)
        DetailRow(stringResource(R.string.driving_license_validity), driver.drivingLicenseValidity)
        DetailRow(
            stringResource(R.string.medical_certificate_validity),
            driver.medicalCertificateValidity
        )
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
