package com.medvedev.presentation.usecase.car

import androidx.lifecycle.LiveData
import com.medvedev.presentation.repository.AppRepository
import com.medvedev.presentation.pojo.Car

class GetCarsListUseCase(private val repository: AppRepository) {
    operator fun invoke(): LiveData<List<Car>> = repository.getCarsList()
}