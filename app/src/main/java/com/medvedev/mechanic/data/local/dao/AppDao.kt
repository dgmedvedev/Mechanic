package com.medvedev.mechanic.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.medvedev.mechanic.data.local.entity.CarEntity
import com.medvedev.mechanic.data.local.entity.DriverEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT*FROM cars ORDER BY stateNumber")
    fun getCarsList(): Flow<List<CarEntity>>

    @Query("SELECT*FROM drivers ORDER BY surname")
    fun getDriversList(): Flow<List<DriverEntity>>

    @Query("SELECT stateNumber FROM cars ORDER BY stateNumber ASC")
    fun getStateNumbersList(): List<String>

    @Query("SELECT surname FROM drivers ORDER BY surname ASC")
    fun getSurnamesList(): List<String>

    @Query("SELECT*FROM cars WHERE id == :id LIMIT 1")
    suspend fun getCarById(id: String?): CarEntity

    @Query("SELECT*FROM drivers WHERE id == :id LIMIT 1")
    suspend fun getDriverById(id: String?): DriverEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarItem(carEntity: CarEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDriverItem(driverEntity: DriverEntity)

    @Delete
    suspend fun deleteCarItem(carEntity: CarEntity)

    @Delete
    suspend fun deleteDriverItem(driverEntity: DriverEntity)
}