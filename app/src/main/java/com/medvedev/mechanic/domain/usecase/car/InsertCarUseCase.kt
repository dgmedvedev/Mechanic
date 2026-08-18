package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.model.Car

class InsertCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(car: Car) = repository.insertCar(car)
}