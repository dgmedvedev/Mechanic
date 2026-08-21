package com.medvedev.mechanic.presentation.drivers.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.usecase.driver.DeleteDriverUseCase
import com.medvedev.mechanic.domain.usecase.driver.GetDriverByIdUseCase
import com.medvedev.mechanic.domain.usecase.driver.InsertDriverUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DriverEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDriverByIdUseCase: GetDriverByIdUseCase,
    private val insertDriverUseCase: InsertDriverUseCase,
    private val deleteDriverUseCase: DeleteDriverUseCase,
) : ViewModel() {

    private val driverId: String? = savedStateHandle["driverId"]

    private val _uiState = MutableStateFlow(DriverEditUiState(isLoading = driverId != null))
    val uiState: StateFlow<DriverEditUiState> = _uiState.asStateFlow()

    init {
        driverId?.let { loadDriver(it) } ?: _uiState.update { it.copy(isLoading = false) }
    }

    fun loadDriver(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { getDriverByIdUseCase(id) }
                .onSuccess { driver ->
                    _uiState.update {
                        it.copy(
                            existingDriver = driver,
                            form = DriverFormState.fromDriver(driver),
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onFormChange(transform: (DriverFormState) -> DriverFormState) {
        _uiState.update { it.copy(form = transform(it.form), errorMessageRes = null) }
    }

    fun saveDriver(defaultName: String, defaultSurname: String) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessageRes = null) }
            val driver = buildDriver(
                existingDriver = state.existingDriver,
                driverId = driverId,
                form = state.form,
                defaultName = defaultName,
                defaultSurname = defaultSurname,
            )
            insertDriverUseCase(driver)
            _uiState.update { it.copy(isSaving = false, saveCompleted = true) }
        }
    }

    fun consumeSaveCompleted() {
        _uiState.update { it.copy(saveCompleted = false) }
    }

    fun consumeError() {
        _uiState.update { it.copy(errorMessageRes = null) }
    }

    private suspend fun buildDriver(
        existingDriver: Driver?,
        driverId: String?,
        form: DriverFormState,
        defaultName: String,
        defaultSurname: String,
    ): Driver {
        var id = driverId ?: System.currentTimeMillis().toString()
        existingDriver?.let {
            id = it.id
            deleteDriverUseCase(it)
        }
        return Driver(
            id = id,
            name = form.name.ifBlank { defaultName },
            surname = form.surname.ifBlank { defaultSurname },
            middleName = form.middleName,
            birthday = form.birthday,
            drivingLicenseNumber = form.drivingLicenseNumber,
            drivingLicenseValidity = form.drivingLicenseValidity,
            medicalCertificateValidity = form.medicalCertificateValidity,
        )
    }
}
