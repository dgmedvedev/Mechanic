package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.model.Car

class DeleteCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(car: Car) = repository.deleteCar(car)
}