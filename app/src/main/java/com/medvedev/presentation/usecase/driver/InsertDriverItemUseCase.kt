package com.medvedev.presentation.usecase.driver

import com.medvedev.presentation.repository.AppRepository
import com.medvedev.presentation.pojo.Driver

class InsertDriverItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(driver: Driver) = repository.insertDriverItem(driver)
}