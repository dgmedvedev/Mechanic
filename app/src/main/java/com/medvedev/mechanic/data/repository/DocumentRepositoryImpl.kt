package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.docs.CachedDocument
import com.medvedev.mechanic.data.docs.DocumentCatalog
import com.medvedev.mechanic.data.docs.DocumentFileDataSource
import com.medvedev.mechanic.data.docs.DocumentMetadata
import com.medvedev.mechanic.data.docs.DocumentProbe
import com.medvedev.mechanic.data.docs.DocumentRemoteDataSource
import com.medvedev.mechanic.data.docs.RemoteDocument
import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.domain.document.DocumentAccess
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.LocalDocument
import com.medvedev.mechanic.domain.repository.DocumentRepository
import com.medvedev.mechanic.domain.result.Result
import javax.inject.Inject

class DocumentRepositoryImpl @Inject constructor(
    private val fileDataSource: DocumentFileDataSource,
    private val remoteDataSource: DocumentRemoteDataSource,
) : DocumentRepository {

    override suspend fun prepareDocument(id: String): Result<DocumentAccess, DomainError> {
        val url = DocumentCatalog.urlFor(id)
            ?: return Result.Error(DomainError.NotFound)

        val cached = when (val result = fileDataSource.read(id)) {
            is Result.Success -> result.data
            is Result.Error -> return Result.Error(result.error.toDomain())
        }

        if (cached != null && cached.metadata.isFresh(REVALIDATE_AFTER_MS)) {
            return Result.Success(DocumentAccess.Ready(cached.toLocal(id, isOfflineCopy = false)))
        }

        return when (val probe = remoteDataSource.probe(url, cached?.metadata)) {
            is Result.Success -> when (val payload = probe.data) {
                DocumentProbe.NotModified -> {
                    val file = cached ?: return Result.Error(DomainError.NotFound)
                    fileDataSource.writeMetadata(
                        id = id,
                        metadata = file.metadata.copy(checkedAtEpochMs = System.currentTimeMillis()),
                    )
                    Result.Success(DocumentAccess.Ready(file.toLocal(id, isOfflineCopy = false)))
                }

                is DocumentProbe.Available -> Result.Success(
                    DocumentAccess.DownloadRequired(
                        sizeBytes = payload.contentLength,
                        cached = cached?.toLocal(id, isOfflineCopy = false),
                    )
                )
            }

            is Result.Error -> cached?.let {
                Result.Success(DocumentAccess.Ready(it.toLocal(id, isOfflineCopy = true)))
            } ?: Result.Error(probe.error.toDomain())
        }
    }

    override suspend fun getDocument(id: String): Result<LocalDocument, DomainError> {
        val url = DocumentCatalog.urlFor(id)
            ?: return Result.Error(DomainError.NotFound)

        val cached = when (val result = fileDataSource.read(id)) {
            is Result.Success -> result.data
            is Result.Error -> return Result.Error(result.error.toDomain())
        }

        if (cached != null && cached.metadata.isFresh(REVALIDATE_AFTER_MS)) {
            return Result.Success(cached.toLocal(id, isOfflineCopy = false))
        }

        val destination = fileDataSource.tempFile(id)

        return when (
            val remote = remoteDataSource.fetch(
                url = url,
                metadata = cached?.metadata,
                destination = destination,
            )
        ) {
            is Result.Success -> when (val payload = remote.data) {
                RemoteDocument.NotModified -> {
                    val file = cached ?: return Result.Error(DomainError.NotFound)
                    fileDataSource.writeMetadata(
                        id = id,
                        metadata = file.metadata.copy(checkedAtEpochMs = System.currentTimeMillis()),
                    )
                    Result.Success(file.toLocal(id, isOfflineCopy = false))
                }

                is RemoteDocument.Downloaded -> replaceOrKeep(
                    id = id,
                    cached = cached,
                    downloaded = payload,
                )
            }

            is Result.Error -> cached.toOfflineResult(id)
                ?: Result.Error(remote.error.toDomain())
        }
    }

    private suspend fun replaceOrKeep(
        id: String,
        cached: CachedDocument?,
        downloaded: RemoteDocument.Downloaded,
    ): Result<LocalDocument, DomainError> {
        val metadata = DocumentMetadata(
            eTag = downloaded.eTag,
            lastModified = downloaded.lastModified,
            contentLength = downloaded.contentLength,
            checkedAtEpochMs = System.currentTimeMillis(),
        )

        return when (val stored = fileDataSource.replace(id, downloaded.file, metadata)) {
            is Result.Success -> Result.Success(
                LocalDocument(
                    id = id,
                    path = stored.data.absolutePath,
                    isOfflineCopy = false,
                )
            )

            is Result.Error -> cached.toOfflineResult(id)
                ?: Result.Error(stored.error.toDomain())
        }
    }

    private fun CachedDocument?.toOfflineResult(id: String): Result<LocalDocument, DomainError>? {
        val cached = this ?: return null
        return Result.Success(cached.toLocal(id, isOfflineCopy = true))
    }

    private fun CachedDocument.toLocal(id: String, isOfflineCopy: Boolean) = LocalDocument(
        id = id,
        path = file.absolutePath,
        isOfflineCopy = isOfflineCopy,
    )

    private companion object {
        const val REVALIDATE_AFTER_MS = 24 * 60 * 60 * 1000L
    }
}
