package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.document.PdfSearchIndex
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.result.Result

interface PdfSearchRepository {
    suspend fun loadIndex(path: String): Result<PdfSearchIndex, DomainError>
}
