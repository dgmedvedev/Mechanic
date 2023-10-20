package com.medvedev.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    @Query("SELECT*FROM cars ORDER BY stateNumber")
    fun getCarsList(): LiveData<List<CarDbModel>>

    @Query("SELECT*FROM drivers ORDER BY surname")
    fun getDriversList(): LiveData<List<DriverDbModel>>

    @Query("SELECT stateNumber FROM cars ORDER BY stateNumber ASC")
    fun getStateNumbersList(): List<String>

    @Query("SELECT surname FROM drivers ORDER BY surname ASC")
    fun getSurnamesList(): List<String>

    @Query("SELECT*FROM cars WHERE id == :id LIMIT 1")
    suspend fun getCarById(id: String?): CarDbModel

    @Query("SELECT*FROM drivers WHERE id == :id LIMIT 1")
    suspend fun getDriverById(id: String?): DriverDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarItem(carDbModel: CarDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriverItem(driverDbModel: DriverDbModel)

    @Delete
    suspend fun deleteCarItem(carDbModel: CarDbModel)

    @Delete
    suspend fun deleteDriverItem(driverDbModel: DriverDbModel)
}