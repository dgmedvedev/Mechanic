package com.medvedev.domain.usecase.car

import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Car

class InsertCarItemUseCase(private val repository: AppRepository) {
    suspend operator fun invoke(car: Car) = repository.insertCarItem(car)
}