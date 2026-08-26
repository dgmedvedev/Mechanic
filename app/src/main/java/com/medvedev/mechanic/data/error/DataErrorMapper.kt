package com.medvedev.mechanic.data.error

import com.medvedev.mechanic.domain.error.DomainError

fun DataError.toDomain(): DomainError {
    return when (this) {
        DataError.NotFound -> DomainError.NotFound

        DataError.Database.ConstraintViolation -> DomainError.Storage.Conflict
        DataError.Database.Corrupted,
        DataError.Database.Full,
        DataError.Database.Locked,
        DataError.Database.ReadOnly,
        DataError.Database.Unknown ->
            DomainError.Storage.Unavailable

        is DataError.Unknown -> DomainError.Unknown
    }
}
