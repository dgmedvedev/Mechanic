package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface CarRepository {

    fun getCars(): Flow<List<Car>>

    suspend fun getCarById(id: String): Result<Car, DomainError>

    suspend fun insertCar(car: Car): Result<Unit, DomainError>

    suspend fun deleteCar(car: Car): Result<Unit, DomainError>
}
