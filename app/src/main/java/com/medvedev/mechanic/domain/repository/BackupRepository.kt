package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.BackupFile
import com.medvedev.mechanic.domain.result.Result

interface BackupRepository {

    suspend fun exportBackup(): Result<BackupFile, DomainError>

    suspend fun saveBackup(
        backup: BackupFile,
        destinationUri: String,
    ): Result<Unit, DomainError>

    suspend fun restoreBackup(sourceUri: String): Result<Unit, DomainError>
}
