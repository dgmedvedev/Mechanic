package com.medvedev.mechanic.domain.usecase.document

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.LocalDocument
import com.medvedev.mechanic.domain.repository.DocumentRepository
import com.medvedev.mechanic.domain.result.Result

class GetDocumentUseCase(private val repository: DocumentRepository) {
    suspend operator fun invoke(id: String): Result<LocalDocument, DomainError> =
        repository.getDocument(id)
}
