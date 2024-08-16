package com.medvedev.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [CarDbModel::class, DriverDbModel::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        private var db: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "mechanic.db"

        fun getInstance(application: Application): AppDatabase {
            db?.let { return it }
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DB_NAME
                )
                    .fallbackToDestructiveMigration()           // temporarily
                    .build()
                db = instance
                return instance
            }
        }
    }

    abstract fun appDao(): AppDao
}