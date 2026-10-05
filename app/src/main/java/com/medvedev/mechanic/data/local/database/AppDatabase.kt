package com.medvedev.mechanic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medvedev.mechanic.data.local.dao.AppDao
import com.medvedev.mechanic.data.local.dao.ExpiryReminderDao
import com.medvedev.mechanic.data.local.entity.CarEntity
import com.medvedev.mechanic.data.local.entity.DriverEntity
import com.medvedev.mechanic.data.local.entity.ExpiryReminderShownEntity

private const val APP_DATABASE_VERSION = 4

@Database(
    entities = [CarEntity::class, DriverEntity::class, ExpiryReminderShownEntity::class],
    version = APP_DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    abstract fun expiryReminderDao(): ExpiryReminderDao

    companion object {
        const val NAME = "mechanic.db"
        const val VERSION = APP_DATABASE_VERSION
    }
}