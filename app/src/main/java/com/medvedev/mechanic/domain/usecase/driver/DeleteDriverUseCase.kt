package com.medvedev.mechanic.domain.usecase.driver

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.repository.DriverRepository
import com.medvedev.mechanic.domain.result.Result

class DeleteDriverUseCase(private val repository: DriverRepository) {
    suspend operator fun invoke(driver: Driver): Result<Unit, DomainError> =
        repository.deleteDriver(driver)
}
