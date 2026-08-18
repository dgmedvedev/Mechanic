package com.medvedev.data.repository

import com.medvedev.data.local.datasource.LocalDataSource
import com.medvedev.data.local.mapper.toDomain
import com.medvedev.data.local.mapper.toEntity
import com.medvedev.domain.model.Driver
import com.medvedev.domain.repository.DriverRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DriverRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : DriverRepository {

    override fun getDrivers(): Flow<List<Driver>> =
        localDataSource.getDrivers().map { it.toDomain() }

    override fun getSurnames(): List<String> =
        localDataSource.getSurnames()

    override suspend fun getDriverById(id: String): Driver =
        localDataSource.getDriverById(id).toDomain()

    override suspend fun insertDriver(driver: Driver) =
        localDataSource.insertDriver(driver.toEntity())

    override suspend fun deleteDriver(driver: Driver) =
        localDataSource.deleteDriver(driver.toEntity())
}
