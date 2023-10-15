package com.medvedev.presentation.usecases

import androidx.lifecycle.LiveData
import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Car

class GetCarsListUseCase(private val repository: AppRepository) {
    operator fun invoke(): LiveData<List<Car>> = repository.getCarsList()
}