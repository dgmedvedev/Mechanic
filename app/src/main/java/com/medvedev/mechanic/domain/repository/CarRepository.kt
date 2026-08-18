package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.model.Car
import kotlinx.coroutines.flow.Flow

interface CarRepository {

    fun getCars(): Flow<List<Car>>

    fun getStateNumbers(): List<String>

    suspend fun getCarById(id: String): Car

    suspend fun insertCar(car: Car)

    suspend fun deleteCar(car: Car)
}
