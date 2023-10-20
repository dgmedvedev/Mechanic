package com.medvedev.presentation.usecase.car

import com.medvedev.presentation.repository.AppRepository

class GetStateNumbersListUseCase(private val repository: AppRepository) {
    operator fun invoke(): List<String> = repository.getStateNumbersList()
}