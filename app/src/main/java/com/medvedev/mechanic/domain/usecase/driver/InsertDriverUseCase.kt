package com.medvedev.mechanic.domain.usecase.driver

import com.medvedev.mechanic.domain.repository.DriverRepository
import com.medvedev.mechanic.domain.model.Driver

class InsertDriverUseCase(private val repository: DriverRepository) {
    suspend operator fun invoke(driver: Driver) = repository.insertDriver(driver)
}