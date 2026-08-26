package com.medvedev.mechanic.data.local.datasource

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.local.entity.CarEntity
import com.medvedev.mechanic.data.local.entity.DriverEntity
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface LocalDataSource {

    fun getCars(): Flow<List<CarEntity>>

    fun getDrivers(): Flow<List<DriverEntity>>

    suspend fun getCarById(id: String): Result<CarEntity, DataError>

    suspend fun getDriverById(id: String): Result<DriverEntity, DataError>

    suspend fun insertCar(car: CarEntity): Result<Unit, DataError>

    suspend fun insertDriver(driver: DriverEntity): Result<Unit, DataError>

    suspend fun deleteCar(car: CarEntity): Result<Unit, DataError>

    suspend fun deleteDriver(driver: DriverEntity): Result<Unit, DataError>
}
