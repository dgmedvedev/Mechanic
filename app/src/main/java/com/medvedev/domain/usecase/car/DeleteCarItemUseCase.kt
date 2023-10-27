package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Car

class DeleteCarItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(car: Car) = repository.deleteCarItem(car)
}