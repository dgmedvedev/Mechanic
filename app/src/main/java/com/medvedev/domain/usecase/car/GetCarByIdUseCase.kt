package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.CarRepository
import com.medvedev.domain.pojo.Car

class GetCarByIdUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(id: String): Car = repository.getCarById(id)
}