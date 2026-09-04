package com.medvedev.mechanic.presentation.drivers.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.result.Result
import com.medvedev.mechanic.domain.usecase.driver.DeleteDriverUseCase
import com.medvedev.mechanic.domain.usecase.driver.GetDriverByIdUseCase
import com.medvedev.mechanic.domain.usecase.driver.InsertDriverUseCase
import com.medvedev.mechanic.presentation.error.toMessageRes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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
            when (val result = getDriverByIdUseCase(id)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            existingDriver = result.data,
                            form = DriverFormState.fromDriver(result.data),
                            isLoading = false,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessageRes = result.error.toMessageRes(),
                        )
                    }
                }
            }
        }
    }

    fun onFormChange(transform: (DriverFormState) -> DriverFormState) {
        _uiState.update { it.copy(form = transform(it.form), errorMessageRes = null) }
    }

    fun saveDriver() {
        val state = _uiState.value
        val form = state.form.normalized()

        if (!form.isValid()) {
            _uiState.update { it.copy(form = form, showFieldErrors = true) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(form = form, isSaving = true, errorMessageRes = null) }
            val result = buildDriver(
                existingDriver = state.existingDriver,
                driverId = driverId,
                form = form,
            )
            when (result) {
                is Result.Success -> save(result.data)
                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isSaving = false,
                            errorMessageRes = result.error.toMessageRes(),
                        )
                    }
                }
            }
        }
    }

    fun consumeSaveCompleted() {
        _uiState.update { it.copy(saveCompleted = false) }
    }

    fun consumeError() {
        _uiState.update { it.copy(errorMessageRes = null) }
    }

    private suspend fun save(driver: Driver) {
        when (val result = insertDriverUseCase(driver)) {
            is Result.Success -> {
                _uiState.update { it.copy(isSaving = false, saveCompleted = true) }
            }

            is Result.Error -> {
                _uiState.update {
                    it.copy(
                        isSaving = false,
                        errorMessageRes = result.error.toMessageRes(),
                    )
                }
            }
        }
    }

    private suspend fun buildDriver(
        existingDriver: Driver?,
        driverId: String?,
        form: DriverFormState,
    ): Result<Driver, DomainError> {
        existingDriver?.let {
            val result = deleteDriverUseCase(it)
            if (result is Result.Error) return result
        }

        val id = existingDriver?.id ?: driverId ?: System.currentTimeMillis().toString()

        return Result.Success(
            Driver(
                id = id,
                name = form.name,
                surname = form.surname,
                middleName = form.middleName,
                birthday = form.birthday,
                drivingLicenseNumber = form.drivingLicenseNumber,
                drivingLicenseValidity = form.drivingLicenseValidity,
                medicalCertificateValidity = form.medicalCertificateValidity,
            )
        )
    }
}
