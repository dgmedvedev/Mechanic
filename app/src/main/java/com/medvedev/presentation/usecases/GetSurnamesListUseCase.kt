package com.medvedev.presentation.usecases

import com.medvedev.presentation.AppRepository

class GetSurnamesListUseCase(private val repository: AppRepository) {
    operator fun invoke(): List<String> = repository.getSurnamesList()
}