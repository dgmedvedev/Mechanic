package com.medvedev.mechanic.domain.usecase.expiry

import com.medvedev.mechanic.domain.model.ReminderSettings
import com.medvedev.mechanic.domain.repository.ReminderSettingsRepository
import kotlinx.coroutines.flow.Flow

class ObserveReminderSettingsUseCase(private val repository: ReminderSettingsRepository) {
    operator fun invoke(): Flow<ReminderSettings> = repository.observeSettings()
}
