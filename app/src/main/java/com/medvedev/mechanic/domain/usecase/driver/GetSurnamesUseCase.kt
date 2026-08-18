package com.medvedev.mechanic.domain.usecase.driver

import com.medvedev.mechanic.domain.repository.DriverRepository

class GetSurnamesUseCase(private val repository: DriverRepository) {
    operator fun invoke(): List<String> = repository.getSurnames()
}