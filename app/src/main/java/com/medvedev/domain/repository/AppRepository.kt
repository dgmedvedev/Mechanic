package com.medvedev.domain.repository

import androidx.lifecycle.LiveData
import com.medvedev.domain.pojo.Car
import com.medvedev.domain.pojo.Driver

interface AppRepository {

    fun getCarsList(): LiveData<List<Car>>

    fun getDriversList(): LiveData<List<Driver>>

    fun getStateNumbersList(): List<String>

    fun getSurnamesList(): List<String>

    suspend fun getCarById(id: String): Car

    suspend fun getDriverById(id: String): Driver

    suspend fun insertCarItem(car: Car)

    suspend fun insertDriverItem(driver: Driver)

    suspend fun deleteCarItem(car: Car)

    suspend fun deleteDriverItem(driver: Driver)
}