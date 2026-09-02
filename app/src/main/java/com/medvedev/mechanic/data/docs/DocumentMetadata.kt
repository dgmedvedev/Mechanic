package com.medvedev.mechanic.data.docs

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.File

@Serializable
data class DocumentMetadata(
    @SerialName("etag")
    val eTag: String? = null,
    val lastModified: String? = null,
    val contentLength: Long? = null,
    val checkedAtEpochMs: Long = 0L,
) {
    fun isFresh(ttlMs: Long, now: Long = System.currentTimeMillis()): Boolean {
        if (checkedAtEpochMs <= 0L) return false
        return now - checkedAtEpochMs < ttlMs
    }
}

data class CachedDocument(
    val file: File,
    val metadata: DocumentMetadata,
)
