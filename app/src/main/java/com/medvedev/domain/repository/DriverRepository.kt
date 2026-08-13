package com.medvedev.domain.repository

import androidx.lifecycle.LiveData
import com.medvedev.domain.pojo.Driver

interface DriverRepository {

    fun getDrivers(): LiveData<List<Driver>>

    fun getSurnames(): List<String>

    suspend fun getDriverById(id: String): Driver

    suspend fun insertDriver(driver: Driver)

    suspend fun deleteDriver(driver: Driver)
}
