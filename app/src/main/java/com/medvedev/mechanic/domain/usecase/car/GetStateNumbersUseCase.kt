package com.medvedev.mechanic.domain.usecase.car

import com.medvedev.mechanic.domain.repository.CarRepository

class GetStateNumbersUseCase(private val repository: CarRepository) {
    operator fun invoke(): List<String> = repository.getStateNumbers()
}