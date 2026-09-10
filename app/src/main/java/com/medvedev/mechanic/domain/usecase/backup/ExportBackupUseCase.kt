package com.medvedev.mechanic.domain.usecase.backup

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.BackupFile
import com.medvedev.mechanic.domain.repository.BackupRepository
import com.medvedev.mechanic.domain.result.Result

class ExportBackupUseCase(private val repository: BackupRepository) {
    suspend operator fun invoke(): Result<BackupFile, DomainError> = repository.exportBackup()
}
