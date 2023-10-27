package com.medvedev.presentation.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.medvedev.data.repository.AppRepositoryImpl
import com.medvedev.domain.pojo.Car
import com.medvedev.domain.usecase.car.DeleteCarItemUseCase
import com.medvedev.domain.usecase.car.GetCarByIdUseCase
import com.medvedev.domain.usecase.car.GetCarsListUseCase
import com.medvedev.domain.usecase.car.InsertCarItemUseCase
import java.util.Locale

class CarViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = AppRepositoryImpl(application)

    private val getCarsListUseCase = GetCarsListUseCase(repository)
    private val getCarByIdUseCase = GetCarByIdUseCase(repository)
    private val insertCarItemUseCase = InsertCarItemUseCase(repository)
    private val deleteCarItemUseCase = DeleteCarItemUseCase(repository)

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    val carListLD = getCarsListUseCase()

    suspend fun getCarById(id: String) = getCarByIdUseCase(id)
    suspend fun insertCar(car: Car) = insertCarItemUseCase(car)
    suspend fun deleteCar(car: Car) = deleteCarItemUseCase(car)

    fun filter(list: List<Car>, desired: String): List<Car> {
        return list.filter {
            it.model.uppercase(Locale.getDefault())
                .plus(it.brand.uppercase(Locale.getDefault()))
                .plus(it.stateNumber.uppercase(Locale.getDefault()))
                .plus(it.yearProduction)
                .contains(desired.uppercase(Locale.getDefault()))
        }
    }

    fun finishWork() {
        _shouldCloseScreen.value = Unit
    }
}