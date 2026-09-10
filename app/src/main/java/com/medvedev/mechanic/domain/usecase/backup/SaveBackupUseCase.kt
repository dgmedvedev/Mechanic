package com.medvedev.mechanic.domain.usecase.backup

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.BackupFile
import com.medvedev.mechanic.domain.repository.BackupRepository
import com.medvedev.mechanic.domain.result.Result

class SaveBackupUseCase(private val repository: BackupRepository) {
    suspend operator fun invoke(
        backup: BackupFile,
        destinationUri: String,
    ): Result<Unit, DomainError> = repository.saveBackup(backup, destinationUri)
}
