package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.result.Result

class InsertCarUseCase(private val repository: CarRepository) {
    suspend operator fun invoke(car: Car): Result<Unit, DomainError> = repository.insertCar(car)
}
