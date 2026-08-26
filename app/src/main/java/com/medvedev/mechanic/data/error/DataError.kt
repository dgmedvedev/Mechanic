package com.medvedev.mechanic.data.error

sealed interface DataError {

    data object NotFound : DataError

    sealed interface Database : DataError {
        data object ConstraintViolation : Database
        data object Corrupted : Database
        data object Full : Database
        data object Locked : Database
        data object ReadOnly : Database
        data object Unknown : Database
    }

    data class Unknown(val cause: Exception) : DataError
}
