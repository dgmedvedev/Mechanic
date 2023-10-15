package com.medvedev.presentation.usecases

import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Driver

class GetDriverByIdUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): Driver = repository.getDriverById(id)
}