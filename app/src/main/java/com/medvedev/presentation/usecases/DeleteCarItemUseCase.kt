package com.medvedev.presentation.usecases

import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Car

class DeleteCarItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(car: Car) = repository.deleteCarItem(car)
}