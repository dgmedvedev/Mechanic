package com.medvedev.domain.usecase.driver

import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Driver

class GetDriverByIdUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): Driver = repository.getDriverById(id)
}