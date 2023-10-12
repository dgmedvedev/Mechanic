package com.medvedev.presentation.usecases

import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Car

class GetCarByIdUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): Car = repository.getCarById(id)
}