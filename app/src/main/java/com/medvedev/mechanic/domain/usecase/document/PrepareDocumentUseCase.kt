package com.medvedev.mechanic.domain.usecase.document

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.document.DocumentAccess
import com.medvedev.mechanic.domain.repository.DocumentRepository
import com.medvedev.mechanic.domain.result.Result

class PrepareDocumentUseCase(private val repository: DocumentRepository) {
    suspend operator fun invoke(id: String): Result<DocumentAccess, DomainError> =
        repository.prepareDocument(id)
}
