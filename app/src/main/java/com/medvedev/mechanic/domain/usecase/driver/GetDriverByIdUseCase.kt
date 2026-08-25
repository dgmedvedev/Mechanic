package com.medvedev.mechanic.domain.usecase.driver

import com.medvedev.mechanic.domain.repository.DriverRepository
import com.medvedev.mechanic.domain.model.Driver

class GetDriverByIdUseCase(private val repository: DriverRepository) {
    suspend operator fun invoke(id: String): Driver = repository.getDriverById(id)
}