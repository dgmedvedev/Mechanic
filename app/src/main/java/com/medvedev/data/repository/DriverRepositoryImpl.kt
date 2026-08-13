package com.medvedev.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.medvedev.data.local.datasource.LocalDataSource
import com.medvedev.data.local.mapper.toDomain
import com.medvedev.data.local.mapper.toEntity
import com.medvedev.domain.pojo.Driver
import com.medvedev.domain.repository.DriverRepository

class DriverRepositoryImpl(
    private val localDataSource: LocalDataSource
) : DriverRepository {

    override fun getDrivers(): LiveData<List<Driver>> = MediatorLiveData<List<Driver>>().apply {
        addSource(localDataSource.getDrivers()) {
            value = it.toDomain()
        }
    }

    override fun getSurnames(): List<String> {
        return localDataSource.getSurnames()
    }

    override suspend fun getDriverById(id: String): Driver {
        val dbModel = localDataSource.getDriverById(id)
        return dbModel.toDomain()
    }

    override suspend fun insertDriver(driver: Driver) {
        localDataSource.insertDriver(driver.toEntity())
    }

    override suspend fun deleteDriver(driver: Driver) {
        localDataSource.deleteDriver(driver.toEntity())
    }
}
