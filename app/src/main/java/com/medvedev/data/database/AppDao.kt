package com.medvedev.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AppDao {
    @Query("SELECT*FROM car_items ORDER BY stateNumber")
    fun getCarsList(): LiveData<List<CarItemDbModel>>

    @Query("SELECT*FROM driver_items ORDER BY surname")
    fun getDriversList(): LiveData<List<DriverItemDbModel>>

    @Query("SELECT stateNumber FROM car_items ORDER BY stateNumber ASC")
    fun getStateNumbersList(): List<String>

    @Query("SELECT surname FROM driver_items ORDER BY surname ASC")
    fun getSurnamesList(): List<String>

    @Query("SELECT*FROM car_items WHERE id == :id LIMIT 1")
    suspend fun getCarById(id: String?): CarItemDbModel

    @Query("SELECT*FROM driver_items WHERE id == :id LIMIT 1")
    suspend fun getDriverById(id: String?): DriverItemDbModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarItem(carItemDbModel: CarItemDbModel)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriverItem(driverItemDbModel: DriverItemDbModel)

    @Delete
    suspend fun deleteCarItem(carItemDbModel: CarItemDbModel)

    @Delete
    suspend fun deleteDriverItem(driverItemDbModel: DriverItemDbModel)
}