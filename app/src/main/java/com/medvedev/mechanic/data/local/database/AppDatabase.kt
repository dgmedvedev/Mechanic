package com.medvedev.mechanic.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.medvedev.mechanic.data.local.dao.AppDao
import com.medvedev.mechanic.data.local.entity.CarEntity
import com.medvedev.mechanic.data.local.entity.DriverEntity

@Database(
    entities = [CarEntity::class, DriverEntity::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun appDao(): AppDao
}