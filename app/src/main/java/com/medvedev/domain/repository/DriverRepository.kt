package com.medvedev.domain.repository

import com.medvedev.domain.model.Driver
import kotlinx.coroutines.flow.Flow

interface DriverRepository {

    fun getDrivers(): Flow<List<Driver>>

    fun getSurnames(): List<String>

    suspend fun getDriverById(id: String): Driver

    suspend fun insertDriver(driver: Driver)

    suspend fun deleteDriver(driver: Driver)
}
