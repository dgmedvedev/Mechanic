package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.DriverRepository
import com.medvedev.domain.model.Driver

class DeleteDriverUseCase(private val repository: DriverRepository) {
    suspend operator fun invoke(driver: Driver) = repository.deleteDriver(driver)
}