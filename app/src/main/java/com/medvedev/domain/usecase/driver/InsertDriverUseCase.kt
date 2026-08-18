package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.DriverRepository
import com.medvedev.domain.model.Driver

class InsertDriverUseCase(private val repository: DriverRepository) {
    suspend operator fun invoke(driver: Driver) = repository.insertDriver(driver)
}