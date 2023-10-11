package com.medvedev.presentation.usecases

import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Car

class InsertCarItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(car: Car) = repository.insertCarItem(car)
}