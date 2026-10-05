package com.medvedev.mechanic.domain.usecase.driver

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.repository.DriverRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.result.Result

class DeleteDriverUseCase(
    private val repository: DriverRepository,
    private val expiryReminderRepository: ExpiryReminderRepository,
    private val scheduler: ExpiryReminderScheduler,
) {
    suspend operator fun invoke(driver: Driver): Result<Unit, DomainError> {
        val result = repository.deleteDriver(driver)
        if (result is Result.Success) {
            expiryReminderRepository.clearEntity(ExpiryEntityType.DRIVER, driver.id)
            scheduler.enqueueImmediate()
        }
        return result
    }
}
