package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.model.Car

class GetCarByIdUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(id: String): Car = repository.getCarById(id)
}