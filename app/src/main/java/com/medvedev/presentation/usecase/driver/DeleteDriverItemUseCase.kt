package com.medvedev.presentation.usecase.driver

import com.medvedev.presentation.repository.AppRepository
import com.medvedev.presentation.pojo.Driver

class DeleteDriverItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(driver: Driver) = repository.deleteDriverItem(driver)
}