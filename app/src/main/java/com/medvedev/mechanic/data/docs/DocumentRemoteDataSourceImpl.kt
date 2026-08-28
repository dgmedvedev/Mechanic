package com.medvedev.mechanic.data.docs

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toNetworkData
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody
import java.io.File
import java.net.HttpURLConnection
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

class DocumentRemoteDataSourceImpl @Inject constructor(
    private val client: OkHttpClient,
) : DocumentRemoteDataSource {

    override suspend fun fetch(
        url: String,
        metadata: DocumentMetadata?,
        destination: File,
    ): Result<RemoteDocument, DataError> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url(url)
            .addValidators(metadata)
            .build()

        try {
            client.newCall(request).execute().use { response ->
                when {
                    response.code == HttpURLConnection.HTTP_NOT_MODIFIED ->
                        Result.Success(RemoteDocument.NotModified)

                    response.code == HttpURLConnection.HTTP_NOT_FOUND ->
                        Result.Error(DataError.Network.NotFound)

                    !response.isSuccessful ->
                        Result.Error(DataError.Network.Unavailable)

                    else -> {
                        val body = response.body
                        when (val written = writeBody(body, destination)) {
                            is Result.Error -> written
                            is Result.Success -> {
                                if (!destination.isPdf()) {
                                    destination.delete()
                                    Result.Error(DataError.Network.InvalidContent)
                                } else {
                                    Result.Success(
                                        RemoteDocument.Downloaded(
                                            file = destination,
                                            eTag = response.header("ETag"),
                                            lastModified = response.header("Last-Modified"),
                                            contentLength = written.data,
                                        )
                                    )
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: CancellationException) {
            destination.delete()
            throw e
        } catch (e: Exception) {
            destination.delete()
            Result.Error(e.toNetworkData())
        }
    }

    override suspend fun probe(
        url: String,
        metadata: DocumentMetadata?,
    ): Result<DocumentProbe, DataError> = withContext(Dispatchers.IO) {
        try {
            probeHead(url, metadata)
                ?: probeRange(url, metadata)
                ?: Result.Success(DocumentProbe.Available(contentLength = null))
        } catch (e: Exception) {
            Result.Error(e.toNetworkData())
        }
    }

    private fun probeHead(
        url: String,
        metadata: DocumentMetadata?,
    ): Result<DocumentProbe, DataError>? {
        val request = Request.Builder()
            .url(url)
            .head()
            .addValidators(metadata)
            .build()
        return client.newCall(request).execute().use { response ->
            response.toProbeResult()
        }
    }

    private fun probeRange(
        url: String,
        metadata: DocumentMetadata?,
    ): Result<DocumentProbe, DataError>? {
        val request = Request.Builder()
            .url(url)
            .header("Range", "bytes=0-0")
            .addValidators(metadata)
            .build()
        return client.newCall(request).execute().use { response ->
            response.toProbeResult()
        }
    }

    private fun Response.toProbeResult(): Result<DocumentProbe, DataError>? {
        return when (code) {
            HttpURLConnection.HTTP_NOT_MODIFIED -> Result.Success(DocumentProbe.NotModified)
            HttpURLConnection.HTTP_NOT_FOUND -> Result.Error(DataError.Network.NotFound)
            HttpURLConnection.HTTP_OK,
            HttpURLConnection.HTTP_PARTIAL -> Result.Success(
                DocumentProbe.Available(contentLength = sizeOrNull())
            )
            else -> if (isSuccessful) {
                Result.Success(DocumentProbe.Available(contentLength = sizeOrNull()))
            } else {
                null
            }
        }
    }

    private fun Request.Builder.addValidators(metadata: DocumentMetadata?): Request.Builder {
        metadata?.eTag?.let { header("If-None-Match", it) }
        metadata?.lastModified?.let { header("If-Modified-Since", it) }
        return this
    }

    private fun Response.sizeOrNull(): Long? {
        header("Content-Length")?.toLongOrNull()?.takeIf { it > 0 }?.let { return it }
        val range = header("Content-Range") ?: return null
        val total = range.substringAfter('/', missingDelimiterValue = "")
        return total.toLongOrNull()?.takeIf { it > 0 }
    }

    private fun writeBody(body: ResponseBody, destination: File): Result<Long, DataError> {
        destination.parentFile?.mkdirs()
        var total = 0L
        body.byteStream().use { input ->
            destination.outputStream().use { output ->
                val buffer = ByteArray(DEFAULT_BUFFER_SIZE)
                while (true) {
                    val read = input.read(buffer)
                    if (read == -1) break
                    total += read
                    if (total > MAX_BYTES) {
                        destination.delete()
                        return Result.Error(DataError.Network.InvalidContent)
                    }
                    output.write(buffer, 0, read)
                }
            }
        }
        return Result.Success(total)
    }

    private fun File.isPdf(): Boolean {
        if (!exists() || length() < PDF_MAGIC.size) return false
        val header = ByteArray(PDF_MAGIC.size)
        inputStream().use { stream ->
            if (stream.read(header) != PDF_MAGIC.size) return false
        }
        return header.contentEquals(PDF_MAGIC)
    }

    private companion object {
        const val MAX_BYTES = 50L * 1024 * 1024
        val PDF_MAGIC = byteArrayOf(0x25, 0x50, 0x44, 0x46) // %PDF
    }
}
