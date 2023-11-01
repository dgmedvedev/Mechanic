package com.medvedev.domain.usecase.car

import androidx.lifecycle.LiveData
import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Car

class GetCarsListUseCase(private val repository: AppRepository) {
    operator fun invoke(): LiveData<List<Car>> = repository.getCarsList()
}