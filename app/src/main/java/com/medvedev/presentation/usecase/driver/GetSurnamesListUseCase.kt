package com.medvedev.presentation.usecase.driver

import com.medvedev.presentation.repository.AppRepository

class GetSurnamesListUseCase(private val repository: AppRepository) {
    operator fun invoke(): List<String> = repository.getSurnamesList()
}