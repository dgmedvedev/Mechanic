package com.medvedev.mechanic.presentation.cars.fuel

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
import com.medvedev.mechanic.presentation.cars.edit.CarEditViewModel
import com.medvedev.mechanic.presentation.cars.edit.CarFormFields
import com.medvedev.mechanic.presentation.components.EditFormLayout

@Composable
fun FuelEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    carId: String? = null,
    embedded: Boolean = false,
    viewModel: CarEditViewModel = hiltViewModel(key = carId?.let { "fuel_edit_$it" }),
) {
    val resources = LocalResources.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    val defaultBrand = stringResource(R.string.brand)
    val defaultModel = stringResource(R.string.model)

    LaunchedEffect(carId) {
        if (carId != null) viewModel.loadCar(carId)
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
        title = stringResource(R.string.menu_button3),
        onClose = onBack,
        embedded = embedded,
        isLoading = uiState.isLoading || (embedded && uiState.existingCar?.id != carId),
        isSaving = uiState.isSaving,
        snackbarHostState = snackbarHostState,
        onSave = {
            viewModel.saveFuelRates(
                defaultBrand = defaultBrand,
                defaultModel = defaultModel,
            )
        },
    ) {
        CarFormFields(
            form = uiState.form,
            onFormChange = viewModel::onFormChange,
            fuelOnly = true,
        )
    }
}
