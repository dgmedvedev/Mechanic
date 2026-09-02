package com.medvedev.mechanic.data.docs.search

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.domain.document.PdfSearchIndex
import com.medvedev.mechanic.domain.result.Result

interface PdfSearchIndexDataSource {
    suspend fun load(path: String): Result<PdfSearchIndex, DataError>
}
