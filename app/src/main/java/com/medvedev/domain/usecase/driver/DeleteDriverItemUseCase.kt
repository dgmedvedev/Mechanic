package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Driver

class DeleteDriverItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(driver: Driver) = repository.deleteDriverItem(driver)
}