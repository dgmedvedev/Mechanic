package com.medvedev.mechanic.domain.error

sealed interface DomainError {

    data object NotFound : DomainError

    sealed interface Storage : DomainError {
        data object Conflict : Storage
        data object Unavailable : Storage
    }

    data object Unknown : DomainError
}
