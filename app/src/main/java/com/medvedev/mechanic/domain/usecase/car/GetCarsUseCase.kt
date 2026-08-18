package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow

class GetCarsUseCase(private val repository: CarRepository) {
    operator fun invoke(): Flow<List<Car>> = repository.getCars()
}
