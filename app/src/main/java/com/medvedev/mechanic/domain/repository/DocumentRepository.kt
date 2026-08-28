package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.document.DocumentAccess
import com.medvedev.mechanic.domain.model.LocalDocument
import com.medvedev.mechanic.domain.result.Result

interface DocumentRepository {

    suspend fun prepareDocument(id: String): Result<DocumentAccess, DomainError>

    suspend fun getDocument(id: String): Result<LocalDocument, DomainError>
}
