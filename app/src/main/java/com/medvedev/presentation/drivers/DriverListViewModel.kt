package com.medvedev.presentation.drivers

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.domain.model.Driver
import com.medvedev.domain.usecase.driver.GetDriversUseCase
import com.medvedev.presentation.common.ListUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DriverListViewModel @Inject constructor(
    getDriversUseCase: GetDriversUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListUiState<Driver>())
    val uiState: StateFlow<ListUiState<Driver>> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            getDriversUseCase().collect { drivers ->
                _uiState.update { state ->
                    state.copy(
                        items = drivers,
                        filteredItems = filter(drivers, state.searchQuery),
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

    private fun filter(list: List<Driver>, query: String): List<Driver> {
        val normalizedQuery = query.trim()
        if (normalizedQuery.isBlank()) return list

        return list.filter { driver ->
            driver.name.contains(normalizedQuery, ignoreCase = true) ||
                    driver.surname.contains(normalizedQuery, ignoreCase = true) ||
                    driver.middleName.contains(normalizedQuery, ignoreCase = true)
        }
    }
}
