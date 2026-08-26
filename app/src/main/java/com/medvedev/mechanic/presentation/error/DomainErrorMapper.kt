package com.medvedev.mechanic.presentation.error

import com.medvedev.mechanic.R
import com.medvedev.mechanic.domain.error.DomainError

fun DomainError.toMessageRes(): Int {
    return when (this) {
        DomainError.NotFound -> R.string.error_not_found

        DomainError.Storage.Conflict -> R.string.error_storage_conflict
        DomainError.Storage.Unavailable -> R.string.error_storage_unavailable

        DomainError.Unknown -> R.string.error_unknown
    }
}
