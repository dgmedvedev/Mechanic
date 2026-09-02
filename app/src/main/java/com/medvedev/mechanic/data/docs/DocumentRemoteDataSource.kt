package com.medvedev.mechanic.data.docs

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.domain.result.Result
import java.io.File

interface DocumentRemoteDataSource {

    suspend fun fetch(
        url: String,
        metadata: DocumentMetadata?,
        destination: File,
    ): Result<RemoteDocument, DataError>

    suspend fun probe(
        url: String,
        metadata: DocumentMetadata?,
    ): Result<DocumentProbe, DataError>
}

sealed interface RemoteDocument {
    data object NotModified : RemoteDocument

    data class Downloaded(
        val file: File,
        val eTag: String?,
        val lastModified: String?,
        val contentLength: Long?,
    ) : RemoteDocument
}

sealed interface DocumentProbe {
    data object NotModified : DocumentProbe
    data class Available(val contentLength: Long?) : DocumentProbe
}
