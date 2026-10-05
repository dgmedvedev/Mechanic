package com.medvedev.mechanic.domain.usecase.expiry

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.repository.ReminderSettingsRepository
import com.medvedev.mechanic.domain.result.Result

class SetRemindersEnabledUseCase(
    private val repository: ReminderSettingsRepository,
    private val scheduler: ExpiryReminderScheduler,
) {
    suspend operator fun invoke(enabled: Boolean): Result<Unit, DomainError> {
        val result = repository.setEnabled(enabled)
        if (result is Result.Success) {
            scheduler.enqueueImmediate()
        }
        return result
    }
}
