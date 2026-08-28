package com.medvedev.mechanic.presentation.error

import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.error.DomainError

fun DomainError.toMessageRes(): Int {
    return when (this) {
        DomainError.NotFound -> R.string.error_not_found

        DomainError.Storage.Conflict -> R.string.error_storage_conflict
        DomainError.Storage.Full -> R.string.error_storage_full
        DomainError.Storage.Unavailable -> R.string.error_storage_unavailable

        DomainError.Network.InvalidContent -> R.string.error_document_invalid
        DomainError.Network.NotFound -> R.string.error_document_not_found
        DomainError.Network.Unavailable -> R.string.error_network_unavailable

        DomainError.Unknown -> R.string.error_unknown
    }
}
