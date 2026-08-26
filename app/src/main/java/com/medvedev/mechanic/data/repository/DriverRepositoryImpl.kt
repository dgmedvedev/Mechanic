package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.data.local.datasource.LocalDataSource
import com.medvedev.mechanic.data.local.mapper.toDomain
import com.medvedev.mechanic.data.local.mapper.toEntity
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.repository.DriverRepository
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : DriverRepository {

    override fun getDrivers(): Flow<List<Driver>> =
        localDataSource.getDrivers().map { it.toDomain() }

    override suspend fun getDriverById(id: String): Result<Driver, DomainError> {
        return when (val result = localDataSource.getDriverById(id)) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun insertDriver(driver: Driver): Result<Unit, DomainError> {
        return when (val result = localDataSource.insertDriver(driver.toEntity())) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun deleteDriver(driver: Driver): Result<Unit, DomainError> {
        return when (val result = localDataSource.deleteDriver(driver.toEntity())) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }
}
