package com.medvedev.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medvedev.data.local.dao.AppDao
import com.medvedev.data.local.entity.CarEntity
import com.medvedev.data.local.entity.DriverEntity

@Database(
    entities = [CarEntity::class, DriverEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}