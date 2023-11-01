package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Car

class GetCarByIdUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(id: String): Car = repository.getCarById(id)
}