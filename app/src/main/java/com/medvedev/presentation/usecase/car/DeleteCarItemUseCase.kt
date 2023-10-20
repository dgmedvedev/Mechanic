package com.medvedev.presentation.usecase.car

import com.medvedev.presentation.repository.AppRepository
import com.medvedev.presentation.pojo.Car

class DeleteCarItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(car: Car) = repository.deleteCarItem(car)
}