package com.medvedev.mechanic.presentation.docs.viewer

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.LruCache
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.core.graphics.createBitmap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import java.io.File

internal class PdfRendererSession private constructor(
    private val pfd: ParcelFileDescriptor,
    private val renderer: PdfRenderer,
) {
    private val pdfDispatcher = Dispatchers.IO.limitedParallelism(1)
    private var closed = false
    private val pageCache = PdfPageCache()

    val pageCount: Int = renderer.pageCount

    suspend fun renderPage(index: Int, widthPx: Int): ImageBitmap = withContext(pdfDispatcher) {
        check(!closed) { "PdfRenderer is closed" }
        ensureActive()
        val width = widthPx.coerceAtLeast(1)
        pageCache.get(index, width)?.let { return@withContext it.asImageBitmap() }

        renderer.openPage(index).use { page ->
            val height = ((page.height.toFloat() / page.width) * width)
                .toInt()
                .coerceAtLeast(1)
            val bitmap = createBitmap(width, height, Bitmap.Config.ARGB_8888)
            bitmap.eraseColor(Color.WHITE)
            val matrix = Matrix().apply {
                val scale = width.toFloat() / page.width
                setScale(scale, scale)
            }
            page.render(bitmap, null, matrix, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            pageCache.put(index, width, bitmap)
            bitmap.asImageBitmap()
        }
    }

    suspend fun close() = withContext(NonCancellable + pdfDispatcher) {
        if (closed) return@withContext
        closed = true
        pageCache.evictAll()
        renderer.close()
        pfd.close()
    }

    companion object {
        suspend fun open(path: String): PdfRendererSession = withContext(Dispatchers.IO) {
            val pfd = ParcelFileDescriptor.open(File(path), ParcelFileDescriptor.MODE_READ_ONLY)
            try {
                PdfRendererSession(pfd, PdfRenderer(pfd))
            } catch (e: Exception) {
                pfd.close()
                throw e
            }
        }
    }
}

private class PdfPageCache {
    private data class CacheKey(
        val pageIndex: Int,
        val widthPx: Int,
    )

    private val cache = object : LruCache<CacheKey, Bitmap>(maxCacheBytes()) {
        override fun sizeOf(key: CacheKey, bitmap: Bitmap): Int = bitmap.byteCount
    }

    fun get(pageIndex: Int, widthPx: Int): Bitmap? =
        cache.get(CacheKey(pageIndex, widthPx))

    fun put(pageIndex: Int, widthPx: Int, bitmap: Bitmap) {
        cache.put(CacheKey(pageIndex, widthPx), bitmap)
    }

    fun evictAll() {
        cache.evictAll()
    }

    private fun maxCacheBytes(): Int {
        val eighthOfHeap = Runtime.getRuntime().maxMemory() / 8
        return eighthOfHeap.coerceAtMost(Int.MAX_VALUE.toLong()).toInt()
    }
}
