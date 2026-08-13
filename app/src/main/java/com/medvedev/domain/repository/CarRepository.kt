package com.medvedev.domain.repository

import androidx.lifecycle.LiveData
import com.medvedev.domain.pojo.Car

interface CarRepository {

    fun getCars(): LiveData<List<Car>>

    fun getStateNumbers(): List<String>

    suspend fun getCarById(id: String): Car

    suspend fun insertCar(car: Car)

    suspend fun deleteCar(car: Car)
}
