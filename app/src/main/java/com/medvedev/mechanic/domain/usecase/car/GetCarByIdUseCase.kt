package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.result.Result

class GetCarByIdUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(id: String): Result<Car, DomainError> = repository.getCarById(id)
}
