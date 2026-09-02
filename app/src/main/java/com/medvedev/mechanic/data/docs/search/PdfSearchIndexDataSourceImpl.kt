package com.medvedev.mechanic.data.docs.search

import android.content.Context
import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toData
import com.medvedev.mechanic.domain.document.PdfSearchIndex
import com.medvedev.mechanic.domain.result.Result
import com.tom_roush.pdfbox.android.PDFBoxResourceLoader
import com.tom_roush.pdfbox.pdmodel.PDDocument
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

class PdfSearchIndexDataSourceImpl @Inject constructor(
    @ApplicationContext context: Context,
) : PdfSearchIndexDataSource {
    init {
        PDFBoxResourceLoader.init(context)
    }

    override suspend fun load(path: String): Result<PdfSearchIndex, DataError> =
        withContext(Dispatchers.IO) {
            try {
                val index = PDDocument.load(File(path)).use { PdfBoxSearchIndex.from(it) }
                Result.Success(index)
            } catch (e: Exception) {
                Result.Error(e.toData())
            }
        }
}
