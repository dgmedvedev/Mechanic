package com.medvedev.mechanic.data.error

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import android.database.sqlite.SQLiteReadOnlyDatabaseException
import android.system.ErrnoException
import android.system.OsConstants
import java.io.IOException
import java.io.InterruptedIOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLException
import kotlin.coroutines.cancellation.CancellationException

fun Exception.toData(): DataError {
    return when (this) {
        is CancellationException -> throw this

        is SQLiteConstraintException -> DataError.Database.ConstraintViolation
        is SQLiteDatabaseCorruptException -> DataError.Database.Corrupted
        is SQLiteFullException -> DataError.Database.Full
        is SQLiteDatabaseLockedException -> DataError.Database.Locked
        is SQLiteReadOnlyDatabaseException -> DataError.Database.ReadOnly
        is SQLiteException -> DataError.Database.Unknown

        is SocketTimeoutException -> DataError.Network.Timeout
        is ConnectException,
        is InterruptedIOException,
        is SocketException,
        is SSLException,
        is UnknownHostException -> DataError.Network.Unavailable

        is ErrnoException if (errno == OsConstants.ENOSPC) -> DataError.File.Full
        is IOException -> DataError.File.Unavailable

        else -> DataError.Unknown(this)
    }
}

fun Exception.toNetworkData(): DataError {
    return when (val mapped = toData()) {
        DataError.File.Unavailable -> DataError.Network.Unavailable
        else -> mapped
    }
}
