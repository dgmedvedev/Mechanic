package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.CarRepository
import com.medvedev.domain.pojo.Car

class InsertCarItemUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(car: Car) = repository.insertCar(car)
}