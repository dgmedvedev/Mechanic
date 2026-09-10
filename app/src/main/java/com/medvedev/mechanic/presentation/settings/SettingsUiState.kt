package com.medvedev.mechanic.presentation.settings

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.BackupFile
import com.medvedev.mechanic.presentation.common.UiState

data class SettingsUiState(
    val createdBackup: BackupFile? = null,
    val isBusy: Boolean = false,
    val error: DomainError? = null,
    val backupSaved: Boolean = false,
    val restoreCompleted: Boolean = false,
) : UiState
