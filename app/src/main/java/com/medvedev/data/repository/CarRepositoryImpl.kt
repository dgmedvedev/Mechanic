package com.medvedev.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.medvedev.data.local.datasource.LocalDataSource
import com.medvedev.data.local.mapper.toDomain
import com.medvedev.data.local.mapper.toEntity
import com.medvedev.domain.pojo.Car
import com.medvedev.domain.repository.CarRepository

class CarRepositoryImpl(
    private val localDataSource: LocalDataSource
) : CarRepository {

    override fun getCars(): LiveData<List<Car>> = MediatorLiveData<List<Car>>().apply {
        addSource(localDataSource.getCars()) {
            value = it.toDomain()
        }
    }

    override fun getStateNumbers(): List<String> {
        return localDataSource.getStateNumbers()
    }

    override suspend fun getCarById(id: String): Car {
        val dbModel = localDataSource.getCarById(id)
        return dbModel.toDomain()
    }

    override suspend fun insertCar(car: Car) {
        localDataSource.insertCar(car.toEntity())
    }

    override suspend fun deleteCar(car: Car) {
        localDataSource.deleteCar(car.toEntity())
    }
}
