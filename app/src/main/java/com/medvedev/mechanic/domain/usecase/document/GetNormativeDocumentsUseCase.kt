package com.medvedev.mechanic.domain.usecase.document

import com.medvedev.mechanic.domain.model.NormativeDocument
import com.medvedev.mechanic.domain.repository.DocumentRepository

class GetNormativeDocumentsUseCase(private val repository: DocumentRepository) {
    operator fun invoke(): List<NormativeDocument> = repository.getNormativeDocuments()
}
