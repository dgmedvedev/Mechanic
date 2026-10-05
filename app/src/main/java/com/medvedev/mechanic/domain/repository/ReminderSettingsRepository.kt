package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ExpiryThreshold
import com.medvedev.mechanic.domain.model.ReminderSettings
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface ReminderSettingsRepository {

    fun observeSettings(): Flow<ReminderSettings>

    suspend fun setEnabled(enabled: Boolean): Result<Unit, DomainError>

    suspend fun setThresholds(thresholds: Set<ExpiryThreshold>): Result<Unit, DomainError>
}
