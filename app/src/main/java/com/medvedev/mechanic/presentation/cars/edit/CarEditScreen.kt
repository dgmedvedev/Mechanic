package com.medvedev.mechanic.presentation.cars.edit

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
import com.medvedev.mechanic.presentation.cars.CarDetailSection
import com.medvedev.mechanic.presentation.components.EditFormLayout

@Composable
fun CarEditScreen(
    onBack: () -> Unit,
    onSaved: () -> Unit,
    carId: String? = null,
    embedded: Boolean = false,
    section: CarDetailSection = CarDetailSection.DATA,
    onSectionChange: (CarDetailSection) -> Unit = {},
    viewModel: CarEditViewModel = hiltViewModel(key = carId?.let { "car_edit_$it" }),
) {
    val resources = LocalResources.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

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
        title = stringResource(R.string.menu_button1),
        onClose = onBack,
        embedded = embedded,
        isLoading = uiState.isLoading || (embedded && uiState.existingCar?.id != carId),
        isSaving = uiState.isSaving,
        snackbarHostState = snackbarHostState,
        onSave = viewModel::saveCar,
    ) {
        CarFormFields(
            form = uiState.form,
            onFormChange = viewModel::onFormChange,
            section = section,
            onSectionChange = onSectionChange,
            showFieldErrors = uiState.showFieldErrors,
        )
    }
}
