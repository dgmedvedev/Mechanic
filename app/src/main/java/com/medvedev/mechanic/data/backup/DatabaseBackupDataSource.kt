package com.medvedev.mechanic.data.backup

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.domain.result.Result
import java.io.File

data class CachedBackup(
    val file: File,
    val displayName: String,
)

interface DatabaseBackupDataSource {

    suspend fun createSnapshot(): Result<CachedBackup, DataError>

    suspend fun writeToUri(path: String, destinationUri: String): Result<Unit, DataError>

    suspend fun restoreFromUri(sourceUri: String): Result<Unit, DataError>
}
