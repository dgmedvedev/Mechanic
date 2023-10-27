package com.medvedev.domain.usecase.driver

import androidx.lifecycle.LiveData
import com.medvedev.domain.repository.AppRepository
import com.medvedev.domain.pojo.Driver

class GetDriversListUseCase(private val repository: AppRepository) {
    operator fun invoke(): LiveData<List<Driver>> = repository.getDriversList()
}