package com.medvedev.mechanic.domain.usecase.backup

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.repository.BackupRepository
import com.medvedev.mechanic.domain.result.Result

class RestoreBackupUseCase(private val repository: BackupRepository) {
    suspend operator fun invoke(sourceUri: String): Result<Unit, DomainError> =
        repository.restoreBackup(sourceUri)
}
