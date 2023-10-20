package com.medvedev.presentation.usecase.car

import com.medvedev.presentation.repository.AppRepository
import com.medvedev.presentation.pojo.Car

class GetCarByIdUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): Car = repository.getCarById(id)
}