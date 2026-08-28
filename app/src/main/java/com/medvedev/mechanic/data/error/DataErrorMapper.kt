package com.medvedev.mechanic.data.error

import com.medvedev.mechanic.domain.error.DomainError

fun DataError.toDomain(): DomainError {
    return when (this) {
        DataError.NotFound -> DomainError.NotFound

        DataError.Database.ConstraintViolation -> DomainError.Storage.Conflict
        DataError.Database.Full, DataError.File.Full -> DomainError.Storage.Full
        DataError.Database.Corrupted,
        DataError.Database.Locked,
        DataError.Database.ReadOnly,
        DataError.Database.Unknown,
        DataError.File.Unavailable ->
            DomainError.Storage.Unavailable

        DataError.Network.InvalidContent -> DomainError.Network.InvalidContent
        DataError.Network.NotFound -> DomainError.Network.NotFound
        DataError.Network.Timeout,
        DataError.Network.Unavailable ->
            DomainError.Network.Unavailable

        is DataError.Unknown -> DomainError.Unknown
    }
}
