package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.CarRepository

class GetStateNumbersUseCase(private val repository: CarRepository) {
    operator fun invoke(): List<String> = repository.getStateNumbers()
}