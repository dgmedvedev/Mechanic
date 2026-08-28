package com.medvedev.mechanic.presentation.docs

import com.medvedev.mechanic.domain.document.DocumentAccess
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.LocalDocument
import com.medvedev.mechanic.presentation.common.UiState

data class PdfDocumentUiState(
    val documentId: String = "",
    val document: LocalDocument? = null,
    val downloadRequired: DocumentAccess.DownloadRequired? = null,
    val isLoading: Boolean = true,
    val error: DomainError? = null,
) : UiState
