package com.medvedev.presentation.usecase.driver

import com.medvedev.presentation.repository.AppRepository
import com.medvedev.presentation.pojo.Driver

class GetDriverByIdUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): Driver = repository.getDriverById(id)
}