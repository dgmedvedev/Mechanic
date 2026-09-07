package com.medvedev.mechanic.presentation.drivers.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Cake
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.MedicalServices
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.PersonOutline
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
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.presentation.components.ConfirmDialog
import com.medvedev.mechanic.presentation.components.DetailContentLayout
import com.medvedev.mechanic.presentation.components.DetailRow
import com.medvedev.mechanic.presentation.components.MechanicTopBar
import com.medvedev.mechanic.presentation.preview.PreviewDriver
import com.medvedev.mechanic.presentation.preview.PreviewMechanicTheme

@Composable
fun DriverDetailsScreen(
    onBack: () -> Unit,
    onNavigateToEdit: (String) -> Unit,
    onDeleted: () -> Unit,
    viewModel: DriverDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val driver = uiState.item

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
                title = stringResource(R.string.drivers),
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
                driver != null -> DriverDetailsContent(
                    driver = driver,
                    onEditClick = { onNavigateToEdit(driver.id) },
                    onDeleteClick = { viewModel.deleteDriver(onDeleted) },
                )
            }
        }
    }
}

@Composable
fun DriverDetailsPane(
    driverId: String,
    onNavigateToEdit: () -> Unit,
    onDeleted: () -> Unit,
    viewModel: DriverDetailsViewModel = hiltViewModel(key = "driver_detail_$driverId"),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val driver = uiState.item

    LaunchedEffect(driverId) {
        viewModel.loadDriver(driverId)
    }

    when {
        uiState.isLoading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        driver != null -> DriverDetailsContent(
            driver = driver,
            onEditClick = onNavigateToEdit,
            onDeleteClick = { viewModel.deleteDriver(onDeleted) },
        )
    }
}

@Composable
fun DriverDetailsContent(
    driver: Driver,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    val showDeleteConfirm = rememberSaveable { mutableStateOf(false) }
    val driverName = "${driver.surname} ${driver.name}".trim()

    DetailContentLayout(
        onEditClick = onEditClick,
        onDeleteClick = { showDeleteConfirm.value = true },
    ) {
        DetailRow(stringResource(R.string.surname), driver.surname, icon = Icons.Outlined.Person)
        DetailRow(stringResource(R.string.name), driver.name, icon = Icons.Outlined.Person)
        DetailRow(
            stringResource(R.string.middle_name),
            driver.middleName,
            icon = Icons.Outlined.PersonOutline,
        )
        DetailRow(stringResource(R.string.birthday), driver.birthday, icon = Icons.Outlined.Cake)
        DetailRow(
            stringResource(R.string.driving_license_number),
            driver.drivingLicenseNumber,
            icon = Icons.Outlined.Badge,
        )
        DetailRow(
            stringResource(R.string.driving_license_validity),
            driver.drivingLicenseValidity,
            icon = Icons.Outlined.Event,
        )
        DetailRow(
            stringResource(R.string.medical_certificate_validity),
            driver.medicalCertificateValidity,
            icon = Icons.Outlined.MedicalServices,
        )
    }

    if (showDeleteConfirm.value) {
        ConfirmDialog(
            title = stringResource(R.string.delete_driver_title),
            text = stringResource(R.string.delete_message, driverName),
            confirmText = stringResource(R.string.delete),
            confirmDestructive = true,
            onConfirm = {
                showDeleteConfirm.value = false
                onDeleteClick()
            },
            onDismiss = { showDeleteConfirm.value = false },
        )
    }
}

@PreviewLightDark
@Composable
private fun DriverDetailsContentPreview() {
    PreviewMechanicTheme {
        DriverDetailsContent(
            driver = PreviewDriver,
            onEditClick = {},
            onDeleteClick = {},
        )
    }
}
