package com.medvedev.domain.usecase.driver

import com.medvedev.domain.model.Driver
import com.medvedev.domain.repository.DriverRepository
import kotlinx.coroutines.flow.Flow

class GetDriversUseCase(private val repository: DriverRepository) {
    operator fun invoke(): Flow<List<Driver>> = repository.getDrivers()
}
