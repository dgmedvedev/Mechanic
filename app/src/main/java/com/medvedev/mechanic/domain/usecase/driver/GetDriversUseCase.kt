package com.medvedev.mechanic.domain.usecase.driver

import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.repository.DriverRepository
import kotlinx.coroutines.flow.Flow

class GetDriversUseCase(private val repository: DriverRepository) {
    operator fun invoke(): Flow<List<Driver>> = repository.getDrivers()
}
