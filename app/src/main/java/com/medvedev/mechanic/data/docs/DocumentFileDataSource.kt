package com.medvedev.mechanic.data.docs

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.domain.result.Result
import java.io.File

interface DocumentFileDataSource {

    fun tempFile(id: String): File

    suspend fun read(id: String): Result<CachedDocument?, DataError>

    suspend fun replace(
        id: String,
        tmpFile: File,
        metadata: DocumentMetadata,
    ): Result<File, DataError>

    suspend fun writeMetadata(id: String, metadata: DocumentMetadata): Result<Unit, DataError>
}
