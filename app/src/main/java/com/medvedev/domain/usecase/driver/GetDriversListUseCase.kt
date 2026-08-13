package com.medvedev.domain.usecase.driver

import androidx.lifecycle.LiveData
import com.medvedev.domain.repository.DriverRepository
import com.medvedev.domain.pojo.Driver

class GetDriversListUseCase(private val repository: DriverRepository) {
    operator fun invoke(): LiveData<List<Driver>> = repository.getDrivers()
}