package com.medvedev.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medvedev.data.local.entity.CarEntity
import com.medvedev.data.local.entity.DriverEntity

@Dao
interface AppDao {
    @Query("SELECT*FROM cars ORDER BY stateNumber")
    fun getCarsList(): LiveData<List<CarEntity>>

    @Query("SELECT*FROM drivers ORDER BY surname")
    fun getDriversList(): LiveData<List<DriverEntity>>

    @Query("SELECT stateNumber FROM cars ORDER BY stateNumber ASC")
    fun getStateNumbersList(): List<String>

    @Query("SELECT surname FROM drivers ORDER BY surname ASC")
    fun getSurnamesList(): List<String>

    @Query("SELECT*FROM cars WHERE id == :id LIMIT 1")
    suspend fun getCarById(id: String?): CarEntity

    @Query("SELECT*FROM drivers WHERE id == :id LIMIT 1")
    suspend fun getDriverById(id: String?): DriverEntity

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertCarItem(carEntity: CarEntity)

    @Insert(onConflict = OnConflictStrategy.Companion.REPLACE)
    suspend fun insertDriverItem(driverEntity: DriverEntity)

    @Delete
    suspend fun deleteCarItem(carEntity: CarEntity)

    @Delete
    suspend fun deleteDriverItem(driverEntity: DriverEntity)
}