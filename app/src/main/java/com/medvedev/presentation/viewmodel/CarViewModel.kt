package com.medvedev.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.medvedev.domain.pojo.Car
import com.medvedev.domain.usecase.car.DeleteCarUseCase
import com.medvedev.domain.usecase.car.GetCarByIdUseCase
import com.medvedev.domain.usecase.car.GetCarsUseCase
import com.medvedev.domain.usecase.car.InsertCarUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class CarViewModel @Inject constructor(
    getCarsUseCase: GetCarsUseCase,
    private val getCarByIdUseCase: GetCarByIdUseCase,
    private val insertCarUseCase: InsertCarUseCase,
    private val deleteCarUseCase: DeleteCarUseCase,
) : ViewModel() {

    private val _shouldCloseScreen = MutableLiveData<Unit>()
    val shouldCloseScreen: LiveData<Unit>
        get() = _shouldCloseScreen

    val carListLD = getCarsUseCase()

    suspend fun getCarById(id: String) = getCarByIdUseCase(id)
    suspend fun insertCar(car: Car) = insertCarUseCase(car)
    suspend fun deleteCar(car: Car) = deleteCarUseCase(car)

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