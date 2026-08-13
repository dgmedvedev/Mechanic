package com.medvedev.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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

    companion object {
        private var db: AppDatabase? = null
        private val LOCK = Any()
        private const val DB_NAME = "mechanic.db"

        fun getInstance(context: Context): AppDatabase {
            db?.let { return it }
            synchronized(LOCK) {
                db?.let { return it }
                val instance = Room.databaseBuilder(
                    context.applicationContext,
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