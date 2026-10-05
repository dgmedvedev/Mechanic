package com.medvedev.mechanic.domain.usecase.expiry

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ExpiryThreshold
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.repository.ReminderSettingsRepository
import com.medvedev.mechanic.domain.result.Result

class SetReminderThresholdsUseCase(
    private val repository: ReminderSettingsRepository,
    private val scheduler: ExpiryReminderScheduler,
) {
    suspend operator fun invoke(thresholds: Set<ExpiryThreshold>): Result<Unit, DomainError> {
        val result = repository.setThresholds(thresholds)
        if (result is Result.Success) {
            scheduler.enqueueImmediate()
        }
        return result
    }
}
