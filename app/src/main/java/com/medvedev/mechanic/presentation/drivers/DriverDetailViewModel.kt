package com.medvedev.mechanic.presentation.drivers

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.model.Driver
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
class DriverDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getDriverByIdUseCase: GetDriverByIdUseCase,
    private val deleteDriverUseCase: DeleteDriverUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailUiState<Driver>())
    val uiState: StateFlow<DetailUiState<Driver>> = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("driverId")?.let(::loadDriver)
    }

    fun loadDriver(driverId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, notFound = false) }
            runCatching { getDriverByIdUseCase(driverId) }
                .onSuccess { driver ->
                    _uiState.update { it.copy(item = driver, isLoading = false) }
                }
                .onFailure {
                    _uiState.update { it.copy(isLoading = false, notFound = true) }
                }
        }
    }

    fun deleteDriver(onDeleted: () -> Unit) {
        val driver = _uiState.value.item ?: return
        viewModelScope.launch {
            deleteDriverUseCase(driver)
            onDeleted()
        }
    }
}
