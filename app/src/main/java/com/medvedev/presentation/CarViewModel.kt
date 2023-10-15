package com.medvedev.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.medvedev.data.repository.AppRepositoryImpl
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.usecases.*
import java.util.Locale

class CarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepositoryImpl(application)

    private val getCarsListUseCase = GetCarsListUseCase(repository)
    private val getCarByIdUseCase = GetCarByIdUseCase(repository)
    private val insertCarItemUseCase = InsertCarItemUseCase(repository)
    private val deleteCarItemUseCase = DeleteCarItemUseCase(repository)

    val carListLD = getCarsListUseCase()

    suspend fun getCarById(id: String) = getCarByIdUseCase(id)
    suspend fun insertCar(car: Car) = insertCarItemUseCase(car)
    suspend fun deleteCar(car: Car) = deleteCarItemUseCase(car)

    fun filter(list: List<Car>, desired: String): List<Car> {
        return list.filter {
            it.stateNumber
                .uppercase(Locale.getDefault())
                .contains(desired.uppercase(Locale.getDefault()))
        }
    }
}