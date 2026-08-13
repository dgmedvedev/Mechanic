package com.medvedev.data.local.datasource

import androidx.lifecycle.LiveData
import com.medvedev.data.local.entity.CarEntity
import com.medvedev.data.local.entity.DriverEntity

interface LocalDataSource {

    fun getCars(): LiveData<List<CarEntity>>

    fun getDrivers(): LiveData<List<DriverEntity>>

    fun getStateNumbers(): List<String>

    fun getSurnames(): List<String>

    suspend fun getCarById(id: String): CarEntity

    suspend fun getDriverById(id: String): DriverEntity

    suspend fun insertCar(car: CarEntity)

    suspend fun insertDriver(driver: DriverEntity)

    suspend fun deleteCar(car: CarEntity)

    suspend fun deleteDriver(driver: DriverEntity)
}
