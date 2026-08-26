package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface DriverRepository {

    fun getDrivers(): Flow<List<Driver>>

    suspend fun getDriverById(id: String): Result<Driver, DomainError>

    suspend fun insertDriver(driver: Driver): Result<Unit, DomainError>

    suspend fun deleteDriver(driver: Driver): Result<Unit, DomainError>
}
