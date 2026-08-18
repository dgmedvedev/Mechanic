package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.DriverRepository
import com.medvedev.domain.model.Driver

class GetDriverByIdUseCase(private val repository: DriverRepository) {
    suspend operator fun invoke(id: String): Driver = repository.getDriverById(id)
}