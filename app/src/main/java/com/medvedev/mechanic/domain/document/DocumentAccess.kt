package com.medvedev.mechanic.domain.document

import com.medvedev.mechanic.domain.model.LocalDocument

sealed interface DocumentAccess {
    data class Ready(val document: LocalDocument) : DocumentAccess

    data class DownloadRequired(
        val sizeBytes: Long?,
        val cached: LocalDocument?,
    ) : DocumentAccess
}
