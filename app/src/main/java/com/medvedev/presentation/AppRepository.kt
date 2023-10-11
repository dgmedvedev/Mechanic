package com.medvedev.presentation

import androidx.lifecycle.LiveData
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.pojo.Driver

interface AppRepository {

    fun getCarsList(): LiveData<List<Car>>

    fun getDriversList(): LiveData<List<Driver>>

    fun getStateNumbersList(): List<String>

    fun getSurnamesList(): List<String>

    suspend fun insertCarItem(car: Car)

    fun insertDriverItem(driver: Driver)

    suspend fun deleteCarItem(car: Car)

    fun deleteDriverItem(driver: Driver)
}