package com.medvedev.mechanic.presentation.drivers.edit

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.compose.ui.res.stringResource
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.components.EditFormLayout

@Composable
fun DriverEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    driverId: String? = null,
    embedded: Boolean = false,
    viewModel: DriverEditViewModel = hiltViewModel(key = driverId?.let { "driver_edit_$it" }),
) {
    val resources = LocalResources.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(driverId) {
        if (driverId != null) viewModel.loadDriver(driverId)
    }

    LaunchedEffect(uiState.saveCompleted) {
        if (uiState.saveCompleted) {
            viewModel.consumeSaveCompleted()
            onSaved()
        }
    }

    LaunchedEffect(uiState.errorMessageRes) {
        uiState.errorMessageRes?.let { messageRes ->
            snackbarHostState.showSnackbar(resources.getString(messageRes))
            viewModel.consumeError()
        }
    }

    EditFormLayout(
        title = stringResource(R.string.menu_button2),
        onClose = onBack,
        embedded = embedded,
        isLoading = uiState.isLoading || (embedded && uiState.existingDriver?.id != driverId),
        isSaving = uiState.isSaving,
        snackbarHostState = snackbarHostState,
        onSave = viewModel::saveDriver,
    ) {
        DriverFormFields(
            form = uiState.form,
            onFormChange = viewModel::onFormChange,
            showFieldErrors = uiState.showFieldErrors,
        )
    }
}
