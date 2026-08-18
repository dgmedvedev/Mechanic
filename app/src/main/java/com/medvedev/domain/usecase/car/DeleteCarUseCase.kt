package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.CarRepository
import com.medvedev.domain.model.Car

class DeleteCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(car: Car) = repository.deleteCar(car)
}