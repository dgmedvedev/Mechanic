package com.medvedev.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CarItemDbModel::class, DriverItemDbModel::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var db: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "mechanic.db"

        fun getInstance(application: Application): AppDatabase {
            db?.let {
                return it
            }
            synchronized(LOCK) {
                db?.let {
                    return it
                }
                val instance = Room.databaseBuilder(application, AppDatabase::class.java, DB_NAME)
                    .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun appDao(): AppDao
}