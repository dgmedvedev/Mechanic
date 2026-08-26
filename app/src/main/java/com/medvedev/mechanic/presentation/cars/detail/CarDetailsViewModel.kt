package com.medvedev.mechanic.presentation.cars.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.result.Result
import com.medvedev.mechanic.domain.usecase.car.DeleteCarUseCase
import com.medvedev.mechanic.domain.usecase.car.GetCarByIdUseCase
import com.medvedev.mechanic.presentation.common.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CarDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val deleteCarUseCase: DeleteCarUseCase,
) : ViewModel() {

    private val carId: String? = savedStateHandle["carId"]

    private val _uiState = MutableStateFlow(DetailUiState<Car>())
    val uiState: StateFlow<DetailUiState<Car>> = _uiState.asStateFlow()

    init {
        carId?.let(::loadCar)
    }

    fun refresh() {
        carId?.let(::loadCar)
    }

    fun loadCar(carId: String) {
        viewModelScope.launch {
            val showLoading = _uiState.value.item == null
            if (showLoading) {
                _uiState.update { it.copy(isLoading = true, notFound = false) }
            }
            when (val result = getCarByIdUseCase(carId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(item = result.data, isLoading = false) }
                }

                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, notFound = true) }
                }
            }
        }
    }

    fun deleteCar(onDeleted: () -> Unit) {
        val car = _uiState.value.item ?: return
        viewModelScope.launch {
            if (deleteCarUseCase(car) is Result.Success) {
                onDeleted()
            }
        }
    }
}
