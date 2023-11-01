package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.AppRepository

class GetStateNumbersListUseCase(private val repository: AppRepository) {
    operator fun invoke(): List<String> = repository.getStateNumbersList()
}