package com.medvedev.presentation.usecases

import com.medvedev.presentation.AppRepository

class GetStateNumbersListUseCase(private val repository: AppRepository) {
    operator fun invoke(): List<String> = repository.getStateNumbersList()
}