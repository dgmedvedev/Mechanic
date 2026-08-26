package com.medvedev.mechanic.data.error

import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabaseCorruptException
import android.database.sqlite.SQLiteDatabaseLockedException
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteFullException
import android.database.sqlite.SQLiteReadOnlyDatabaseException
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

        else -> DataError.Unknown(this)
    }
}
