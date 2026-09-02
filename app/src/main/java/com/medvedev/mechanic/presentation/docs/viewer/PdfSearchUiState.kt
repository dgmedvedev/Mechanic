package com.medvedev.mechanic.presentation.docs.viewer

import com.medvedev.mechanic.domain.document.PdfSearchIndex
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.PdfSearchMatch
import com.medvedev.mechanic.presentation.common.UiState

internal data class PdfSearchUiState(
    val index: PdfSearchIndex? = null,
    val error: DomainError? = null,
    val matches: List<PdfSearchMatch> = emptyList(),
    val matchIndex: Int = 0,
    val committedQuery: String? = null,
) : UiState
