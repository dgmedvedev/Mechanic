package com.medvedev.mechanic.presentation.cars.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.usecase.car.GetCarsUseCase
import com.medvedev.mechanic.presentation.common.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CarListViewModel @Inject constructor(
    getCarsUseCase: GetCarsUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState<Car>())
    val uiState: StateFlow<ListUiState<Car>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getCarsUseCase().collect { cars ->
                _uiState.update { state ->
                    state.copy(
                        items = cars,
                        filteredItems = filter(cars, state.searchQuery),
                        isLoading = false,
                    )
                }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { state ->
            state.copy(
                filteredItems = filter(state.items, query),
                searchQuery = query,
            )
        }
    }

    private fun filter(list: List<Car>, query: String): List<Car> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return list

        return list.filter { car ->
            car.brand.contains(normalizedQuery, ignoreCase = true) ||
                    car.model.contains(normalizedQuery, ignoreCase = true) ||
                    car.stateNumber.contains(normalizedQuery, ignoreCase = true) ||
                    car.yearProduction.toString().contains(normalizedQuery, ignoreCase = true)
        }
    }
}
