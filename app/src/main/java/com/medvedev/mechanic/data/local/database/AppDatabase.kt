package com.medvedev.mechanic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medvedev.mechanic.data.local.dao.AppDao
import com.medvedev.mechanic.data.local.entity.CarEntity
import com.medvedev.mechanic.data.local.entity.DriverEntity

private const val APP_DATABASE_VERSION = 3

@Database(
    entities = [CarEntity::class, DriverEntity::class],
    version = APP_DATABASE_VERSION,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        const val NAME = "mechanic.db"
        const val VERSION = APP_DATABASE_VERSION
    }
}