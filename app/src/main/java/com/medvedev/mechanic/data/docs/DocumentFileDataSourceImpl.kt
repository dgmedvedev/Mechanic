package com.medvedev.mechanic.data.docs

import android.content.Context
import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toData
import com.medvedev.mechanic.domain.result.Result
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import java.io.File
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class DocumentFileDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : DocumentFileDataSource {
    private val docsDir = File(context.filesDir, DOCS_DIR)

    override fun tempFile(id: String): File = File(docsDir, "${safeId(id)}.pdf.tmp")

    override suspend fun read(id: String): Result<CachedDocument?, DataError> =
        withContext(Dispatchers.IO) {
            try {
                val pdf = pdfFile(id)
                if (!pdf.exists() || pdf.length() == 0L) {
                    return@withContext Result.Success(null)
                }
                Result.Success(CachedDocument(file = pdf, metadata = readMetadata(id)))
            } catch (e: Exception) {
                Result.Error(e.toData())
            }
        }

    override suspend fun replace(
        id: String,
        tmpFile: File,
        metadata: DocumentMetadata,
    ): Result<File, DataError> = withContext(Dispatchers.IO) {
        try {
            docsDir.mkdirs()
            val target = pdfFile(id)
            if (!tmpFile.renameTo(target)) {
                tmpFile.copyTo(target, overwrite = true)
                tmpFile.delete()
            }
            persistMetadata(id, metadata)
            Result.Success(target)
        } catch (e: Exception) {
            tmpFile.delete()
            Result.Error(e.toData())
        }
    }

    override suspend fun writeMetadata(
        id: String,
        metadata: DocumentMetadata,
    ): Result<Unit, DataError> = withContext(Dispatchers.IO) {
        try {
            docsDir.mkdirs()
            persistMetadata(id, metadata)
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    private fun readMetadata(id: String): DocumentMetadata {
        val file = metadataFile(id)
        if (!file.exists()) return DocumentMetadata()
        return try {
            json.decodeFromString<DocumentMetadata>(file.readText())
        } catch (e: CancellationException) {
            throw e
        } catch (_: Exception) {
            DocumentMetadata()
        }
    }

    private fun persistMetadata(id: String, metadata: DocumentMetadata) {
        metadataFile(id).writeText(json.encodeToString(metadata))
    }

    private fun pdfFile(id: String) = File(docsDir, "${safeId(id)}.pdf")

    private fun metadataFile(id: String) = File(docsDir, "${safeId(id)}.json")

    private fun safeId(id: String): String {
        require(id.matches(SAFE_ID)) { "Invalid document id" }
        return id
    }

    private companion object {
        const val DOCS_DIR = "docs"
        val SAFE_ID = Regex("^[A-Za-z0-9_-]+$")
        val json = Json { ignoreUnknownKeys = true }
    }
}
