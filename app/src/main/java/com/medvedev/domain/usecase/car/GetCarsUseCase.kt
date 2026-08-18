package com.medvedev.domain.usecase.car

import com.medvedev.domain.model.Car
import com.medvedev.domain.repository.CarRepository
import kotlinx.coroutines.flow.Flow

class GetCarsUseCase(private val repository: CarRepository) {
    operator fun invoke(): Flow<List<Car>> = repository.getCars()
}
