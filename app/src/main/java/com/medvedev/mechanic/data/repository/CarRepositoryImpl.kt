package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.local.datasource.LocalDataSource
import com.medvedev.mechanic.data.local.mapper.toDomain
import com.medvedev.mechanic.data.local.mapper.toEntity
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : CarRepository {

    override fun getCars(): Flow<List<Car>> =
        localDataSource.getCars().map { it.toDomain() }

    override fun getStateNumbers(): List<String> =
        localDataSource.getStateNumbers()

    override suspend fun getCarById(id: String): Car =
        localDataSource.getCarById(id).toDomain()

    override suspend fun insertCar(car: Car) =
        localDataSource.insertCar(car.toEntity())


    override suspend fun deleteCar(car: Car) =
        localDataSource.deleteCar(car.toEntity())
}
