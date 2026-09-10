package com.medvedev.mechanic.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.medvedev.mechanic.domain.result.Result
import com.medvedev.mechanic.domain.usecase.backup.ExportBackupUseCase
import com.medvedev.mechanic.domain.usecase.backup.RestoreBackupUseCase
import com.medvedev.mechanic.domain.usecase.backup.SaveBackupUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val exportBackupUseCase: ExportBackupUseCase,
    private val saveBackupUseCase: SaveBackupUseCase,
    private val restoreBackupUseCase: RestoreBackupUseCase,
) : ViewModel() {

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    fun exportBackup() {
        if (_uiState.value.isBusy) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(isBusy = true, error = null, backupSaved = false)
            }
            when (val result = exportBackupUseCase()) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(isBusy = false, createdBackup = result.data)
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isBusy = false,
                            error = result.error,
                        )
                    }
                }
            }
        }
    }

    fun saveBackup(destinationUri: String) {
        val backup = _uiState.value.createdBackup ?: return
        if (_uiState.value.isBusy) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(isBusy = true, error = null, backupSaved = false)
            }
            when (val result = saveBackupUseCase(backup, destinationUri)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(
                            isBusy = false,
                            createdBackup = null,
                            backupSaved = true,
                        )
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isBusy = false,
                            error = result.error,
                        )
                    }
                }
            }
        }
    }

    fun restoreBackup(sourceUri: String) {
        if (_uiState.value.isBusy) return
        viewModelScope.launch {
            _uiState.update {
                it.copy(isBusy = true, error = null, backupSaved = false)
            }
            when (val result = restoreBackupUseCase(sourceUri)) {
                is Result.Success -> {
                    _uiState.update {
                        it.copy(isBusy = false, restoreCompleted = true)
                    }
                }

                is Result.Error -> {
                    _uiState.update {
                        it.copy(
                            isBusy = false,
                            error = result.error,
                        )
                    }
                }
            }
        }
    }

    fun dismissCreatedBackup() {
        _uiState.update { it.copy(createdBackup = null) }
    }

    fun consumeError() {
        _uiState.update { it.copy(error = null) }
    }

    fun consumeBackupSaved() {
        _uiState.update { it.copy(backupSaved = false) }
    }
}
