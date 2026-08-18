package com.medvedev.presentation.cars

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.domain.model.Car
import com.medvedev.domain.usecase.car.DeleteCarUseCase
import com.medvedev.domain.usecase.car.GetCarByIdUseCase
import com.medvedev.presentation.common.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CarDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val deleteCarUseCase: DeleteCarUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState<Car>())
    val uiState: StateFlow<DetailUiState<Car>> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("carId")?.let(::loadCar)
    }

    fun loadCar(carId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, notFound = false) }
            runCatching { getCarByIdUseCase(carId) }
                .onSuccess { car ->
                    _uiState.update { it.copy(item = car, isLoading = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, notFound = true) }
                }
        }
    }

    fun deleteCar(onDeleted: () -> Unit) {
        val car = _uiState.value.item ?: return
        viewModelScope.launch {
            deleteCarUseCase(car)
            onDeleted()
        }
    }
}
