package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.DriverRepository
import com.medvedev.domain.pojo.Driver

class InsertDriverItemUseCase(private val repository: DriverRepository) {
    suspend operator fun invoke(driver: Driver) = repository.insertDriver(driver)
}