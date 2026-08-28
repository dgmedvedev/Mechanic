package com.medvedev.mechanic.presentation.docs

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.Closeable
import java.io.File
import androidx.core.graphics.createBitmap

class PdfRendererSession private constructor(
    private val pfd: ParcelFileDescriptor,
    private val renderer: PdfRenderer,
) : Closeable {
    private val lock = Any()
    private var closed = false

    val pageCount: Int = renderer.pageCount

    suspend fun renderPage(index: Int, widthPx: Int): Bitmap = withContext(Dispatchers.IO) {
        synchronized(lock) {
            check(!closed) { "PdfRenderer is closed" }
            renderer.openPage(index).use { page ->
                val width = widthPx.coerceAtLeast(1)
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
                bitmap
            }
        }
    }

    override fun close() {
        synchronized(lock) {
            if (closed) return
            closed = true
            renderer.close()
            pfd.close()
        }
    }

    companion object {
        suspend fun open(file: File): PdfRendererSession = withContext(Dispatchers.IO) {
            val pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
            try {
                PdfRendererSession(pfd, PdfRenderer(pfd))
            } catch (e: Exception) {
                pfd.close()
                throw e
            }
        }
    }
}
