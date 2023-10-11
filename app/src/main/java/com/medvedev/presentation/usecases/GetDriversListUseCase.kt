package com.medvedev.presentation.usecases

import androidx.lifecycle.LiveData
import com.medvedev.presentation.AppRepository
import com.medvedev.presentation.pojo.Driver

class GetDriversListUseCase(private val repository: AppRepository) {
    operator fun invoke(): LiveData<List<Driver>> = repository.getDriversList()
}