package com.medvedev.mechanic.domain.usecase.document

import com.medvedev.mechanic.domain.document.PdfSearchIndex
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.repository.PdfSearchRepository
import com.medvedev.mechanic.domain.result.Result

class LoadPdfSearchIndexUseCase(private val repository: PdfSearchRepository) {
    suspend operator fun invoke(path: String): Result<PdfSearchIndex, DomainError> =
        repository.loadIndex(path)
}
