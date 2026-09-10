package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.backup.DatabaseBackupDataSource
import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.BackupFile
import com.medvedev.mechanic.domain.repository.BackupRepository
import com.medvedev.mechanic.domain.result.Result
import javax.inject.Inject

class BackupRepositoryImpl @Inject constructor(
    private val dataSource: DatabaseBackupDataSource,
) : BackupRepository {

    override suspend fun exportBackup(): Result<BackupFile, DomainError> {
        return when (val result = dataSource.createSnapshot()) {
            is Result.Success -> Result.Success(
                BackupFile(
                    path = result.data.file.absolutePath,
                    displayName = result.data.displayName,
                ),
            )

            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun saveBackup(
        backup: BackupFile,
        destinationUri: String,
    ): Result<Unit, DomainError> {
        return when (val result = dataSource.writeToUri(backup.path, destinationUri)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun restoreBackup(sourceUri: String): Result<Unit, DomainError> {
        return when (val result = dataSource.restoreFromUri(sourceUri)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }
}
