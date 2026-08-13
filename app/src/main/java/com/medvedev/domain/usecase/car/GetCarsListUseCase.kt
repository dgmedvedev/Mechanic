package com.medvedev.domain.usecase.car

import androidx.lifecycle.LiveData
import com.medvedev.domain.repository.CarRepository
import com.medvedev.domain.pojo.Car

class GetCarsListUseCase(private val repository: CarRepository) {
    operator fun invoke(): LiveData<List<Car>> = repository.getCars()
}