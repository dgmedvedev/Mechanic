package com.medvedev.presentation.usecases

import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Driver

class InsertDriverItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(driver: Driver) = repository.insertDriverItem(driver)
}