package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.data.local.datasource.LocalDataSource
import com.medvedev.mechanic.data.local.mapper.toDomain
import com.medvedev.mechanic.data.local.mapper.toEntity
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CarRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : CarRepository {

    override fun getCars(): Flow<List<Car>> =
        localDataSource.getCars().map { it.toDomain() }

    override suspend fun getCarById(id: String): Result<Car, DomainError> {
        return when (val result = localDataSource.getCarById(id)) {
            is Result.Success -> Result.Success(result.data.toDomain())
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun insertCar(car: Car): Result<Unit, DomainError> {
        return when (val result = localDataSource.insertCar(car.toEntity())) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun deleteCar(car: Car): Result<Unit, DomainError> {
        return when (val result = localDataSource.deleteCar(car.toEntity())) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }
}
