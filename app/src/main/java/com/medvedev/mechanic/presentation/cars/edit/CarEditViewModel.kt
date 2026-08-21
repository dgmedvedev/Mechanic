package com.medvedev.mechanic.presentation.cars.edit

import android.util.Patterns
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.usecase.car.DeleteCarUseCase
import com.medvedev.mechanic.domain.usecase.car.GetCarByIdUseCase
import com.medvedev.mechanic.domain.usecase.car.InsertCarUseCase
import com.medvedev.mechanic.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
            runCatching { getCarByIdUseCase(id) }
                .onSuccess { car ->
                    _uiState.update {
                        it.copy(
                            existingCar = car,
                            form = CarFormState.fromCar(car),
                            isLoading = false,
                        )
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false) }
                }
        }
    }

    fun onFormChange(transform: (CarFormState) -> CarFormState) {
        _uiState.update { it.copy(form = transform(it.form), errorMessageRes = null) }
    }

    fun saveCar(imageUrl: String, defaultBrand: String, defaultModel: String) {
        val state = _uiState.value
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessageRes = null) }
            val result = buildCar(
                existingCar = state.existingCar,
                carId = carId,
                form = state.form,
                imageUrl = imageUrl,
                defaultBrand = defaultBrand,
                defaultModel = defaultModel,
            )
            when (result) {
                is SaveResult.Success -> {
                    insertCarUseCase(result.car)
                    _uiState.update { it.copy(isSaving = false, saveCompleted = true) }
                }
                is SaveResult.Error -> {
                    _uiState.update { it.copy(isSaving = false, errorMessageRes = result.messageRes) }
                }
            }
        }
    }

    fun saveFuelRates(imageUrl: String, defaultBrand: String, defaultModel: String) {
        val state = _uiState.value
        val existing = state.existingCar ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessageRes = null) }
            val result = buildCar(
                existingCar = existing,
                carId = carId,
                form = state.form,
                imageUrl = imageUrl,
                defaultBrand = defaultBrand,
                defaultModel = defaultModel,
                preserveNonFuelFieldsFrom = existing,
            )
            when (result) {
                is SaveResult.Success -> {
                    insertCarUseCase(result.car)
                    _uiState.update { it.copy(isSaving = false, saveCompleted = true) }
                }
                is SaveResult.Error -> {
                    _uiState.update { it.copy(isSaving = false, errorMessageRes = result.messageRes) }
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

    private sealed interface SaveResult {
        data class Success(val car: Car) : SaveResult
        data class Error(val messageRes: Int) : SaveResult
    }

    private suspend fun buildCar(
        existingCar: Car?,
        carId: String?,
        form: CarFormState,
        imageUrl: String,
        defaultBrand: String,
        defaultModel: String,
        preserveNonFuelFieldsFrom: Car? = null,
    ): SaveResult {
        return try {
            if (!Patterns.WEB_URL.matcher(imageUrl).matches()) {
                return SaveResult.Error(R.string.not_valid_url)
            }
            val year = form.yearProduction.toInt()
            var id = carId ?: System.currentTimeMillis().toString()

            existingCar?.let {
                id = it.id
                deleteCarUseCase(it)
            }

            val base = preserveNonFuelFieldsFrom ?: existingCar
            SaveResult.Success(
                Car(
                    id = id,
                    brand = form.brand.ifBlank { defaultBrand },
                    model = form.model.ifBlank { defaultModel },
                    imageUrl = imageUrl,
                    yearProduction = year,
                    stateNumber = form.stateNumber,
                    vin = base?.vin ?: form.vin,
                    engineDisplacement = base?.engineDisplacement ?: form.engineDisplacement,
                    fuelType = base?.fuelType ?: form.fuelType,
                    allowableWeight = base?.allowableWeight ?: form.allowableWeight,
                    technicalPassport = base?.technicalPassport ?: form.technicalPassport,
                    checkup = base?.checkup ?: form.checkup,
                    insurance = base?.insurance ?: form.insurance,
                    hullInsurance = base?.hullInsurance ?: form.hullInsurance,
                    linearFuelConsumptionRate = form.linearFcr,
                    summerInCityFuelConsumptionRate = form.summerInCityFcr,
                    summerOutCityFuelConsumptionRate = form.summerOutCityFcr,
                    winterInCityFuelConsumptionRate = form.winterInCityFcr,
                    winterOutCityFuelConsumptionRate = form.winterOutCityFcr,
                ),
            )
        } catch (_: NumberFormatException) {
            SaveResult.Error(R.string.enter_year_production)
        }
    }
}
