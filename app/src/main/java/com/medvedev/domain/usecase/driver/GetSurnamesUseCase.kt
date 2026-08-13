package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.DriverRepository

class GetSurnamesUseCase(private val repository: DriverRepository) {
    operator fun invoke(): List<String> = repository.getSurnames()
}