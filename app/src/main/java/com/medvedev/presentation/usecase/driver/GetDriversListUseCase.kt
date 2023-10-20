package com.medvedev.presentation.usecase.driver

import androidx.lifecycle.LiveData
import com.medvedev.presentation.repository.AppRepository
import com.medvedev.presentation.pojo.Driver

class GetDriversListUseCase(private val repository: AppRepository) {
    operator fun invoke(): LiveData<List<Driver>> = repository.getDriversList()
}