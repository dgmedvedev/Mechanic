package com.medvedev.mechanic.presentation.drivers.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.result.Result
import com.medvedev.mechanic.domain.usecase.driver.DeleteDriverUseCase
import com.medvedev.mechanic.domain.usecase.driver.GetDriverByIdUseCase
import com.medvedev.mechanic.presentation.common.DetailUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class DriverDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDriverByIdUseCase: GetDriverByIdUseCase,
    private val deleteDriverUseCase: DeleteDriverUseCase,
) : ViewModel() {

    private val driverId: String? = savedStateHandle["driverId"]

    private val _uiState = MutableStateFlow(DetailUiState<Driver>())
    val uiState: StateFlow<DetailUiState<Driver>> = _uiState.asStateFlow()

    init {
        driverId?.let(::loadDriver)
    }

    fun refresh() {
        driverId?.let(::loadDriver)
    }

    fun loadDriver(driverId: String) {
        viewModelScope.launch {
            val showLoading = _uiState.value.item == null
            if (showLoading) {
                _uiState.update { it.copy(isLoading = true, notFound = false) }
            }
            when (val result = getDriverByIdUseCase(driverId)) {
                is Result.Success -> {
                    _uiState.update { it.copy(item = result.data, isLoading = false) }
                }

                is Result.Error -> {
                    _uiState.update { it.copy(isLoading = false, notFound = true) }
                }
            }
        }
    }

    fun deleteDriver(onDeleted: () -> Unit) {
        val driver = _uiState.value.item ?: return
        viewModelScope.launch {
            if (deleteDriverUseCase(driver) is Result.Success) {
                onDeleted()
            }
        }
    }
}
