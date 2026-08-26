package com.medvedev.mechanic.presentation.cars.edit

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.presentation.error.toMessageRes
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.result.Result
import com.medvedev.mechanic.domain.usecase.car.DeleteCarUseCase
import com.medvedev.mechanic.domain.usecase.car.GetCarByIdUseCase
import com.medvedev.mechanic.domain.usecase.car.InsertCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val insertCarUseCase: InsertCarUseCase,
    private val deleteCarUseCase: DeleteCarUseCase,
) : ViewModel() {

    private val carId: String? = savedStateHandle["carId"]

    private val _uiState = MutableStateFlow(CarEditUiState(isLoading = carId != null))
    val uiState: StateFlow<CarEditUiState> = _uiState.asStateFlow()

    init {
        carId?.let { loadCar(it) } ?: _uiState.update { it.copy(isLoading = false) }
    }

    fun loadCar(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            when (val result = getCarByIdUseCase(id)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            existingCar = result.data,
                            form = CarFormState.fromCar(result.data),
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

    fun onFormChange(transform: (CarFormState) -> CarFormState) {
        _uiState.update { it.copy(form = transform(it.form), errorMessageRes = null) }
    }

    fun saveCar() {
        val state = _uiState.value

        validateForm(state.form)?.let { errorRes ->
            _uiState.update { it.copy(errorMessageRes = errorRes) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessageRes = null) }
            val result = buildCar(
                existingCar = state.existingCar,
                carId = carId,
                form = state.form,
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

    fun saveFuelRates() {
        val state = _uiState.value
        val existing = state.existingCar ?: return

        validateForm(state.form)?.let { errorRes ->
            _uiState.update { it.copy(errorMessageRes = errorRes) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessageRes = null) }
            val result = buildCar(
                existingCar = existing,
                carId = carId,
                form = state.form,
                preserveNonFuelFieldsFrom = existing,
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

    private fun validateForm(form: CarFormState): Int? {
        form.yearProduction.toIntOrNull() ?: return R.string.enter_year_production
        if (form.brand.isBlank()) return R.string.enter_brand
        if (form.model.isBlank()) return R.string.enter_model
        return null
    }

    private suspend fun save(car: Car) {
        when (val result = insertCarUseCase(car)) {
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

    private suspend fun buildCar(
        existingCar: Car?,
        carId: String?,
        form: CarFormState,
        preserveNonFuelFieldsFrom: Car? = null,
    ): Result<Car, DomainError> {
        existingCar?.let { car ->
            val deleteResult = deleteCarUseCase(car)
            if (deleteResult is Result.Error) return deleteResult
        }

        val id = existingCar?.id ?: carId ?: System.currentTimeMillis().toString()

        return Result.Success(
            Car(
                id = id,
                brand = form.brand,
                model = form.model,
                yearProduction = form.yearProduction.toInt(),
                stateNumber = form.stateNumber,
                vin = preserveNonFuelFieldsFrom?.vin ?: form.vin,
                engineDisplacement = preserveNonFuelFieldsFrom?.engineDisplacement
                    ?: form.engineDisplacement,
                fuelType = preserveNonFuelFieldsFrom?.fuelType ?: form.fuelType,
                allowableWeight = preserveNonFuelFieldsFrom?.allowableWeight
                    ?: form.allowableWeight,
                technicalPassport = preserveNonFuelFieldsFrom?.technicalPassport
                    ?: form.technicalPassport,
                checkup = preserveNonFuelFieldsFrom?.checkup ?: form.checkup,
                insurance = preserveNonFuelFieldsFrom?.insurance ?: form.insurance,
                hullInsurance = preserveNonFuelFieldsFrom?.hullInsurance ?: form.hullInsurance,
                linearFuelConsumptionRate = form.linearFcr,
                summerInCityFuelConsumptionRate = form.summerInCityFcr,
                summerOutCityFuelConsumptionRate = form.summerOutCityFcr,
                winterInCityFuelConsumptionRate = form.winterInCityFcr,
                winterOutCityFuelConsumptionRate = form.winterOutCityFcr,
            )
        )
    }
}
