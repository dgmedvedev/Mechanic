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

    sealed interface File : DataError {
        data object Full : File
        data object Unavailable : File
    }

    sealed interface Network : DataError {
        data object InvalidContent : Network
        data object NotFound : Network
        data object Timeout : Network
        data object Unavailable : Network
    }

    data class Unknown(val cause: Exception) : DataError
}
