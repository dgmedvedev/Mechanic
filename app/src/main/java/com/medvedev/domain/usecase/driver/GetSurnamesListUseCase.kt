package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.AppRepository

class GetSurnamesListUseCase(private val repository: AppRepository) {
    operator fun invoke(): List<String> = repository.getSurnamesList()
}