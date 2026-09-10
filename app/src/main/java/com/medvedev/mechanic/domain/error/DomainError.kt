package com.medvedev.mechanic.domain.error

sealed interface DomainError {

    data object NotFound : DomainError

    sealed interface Storage : DomainError {
        data object Conflict : Storage
        data object Full : Storage
        data object Unavailable : Storage
    }

    sealed interface Network : DomainError {
        data object InvalidContent : Network
        data object NotFound : Network
        data object Unavailable : Network
    }

    sealed interface Backup : DomainError {
        data object Incompatible : Backup
        data object Invalid : Backup
    }

    data object Unknown : DomainError
}
