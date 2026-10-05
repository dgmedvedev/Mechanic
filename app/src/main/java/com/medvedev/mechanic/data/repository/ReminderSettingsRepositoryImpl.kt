package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.data.preferences.ReminderPreferencesDataSource
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ExpiryThreshold
import com.medvedev.mechanic.domain.model.ReminderSettings
import com.medvedev.mechanic.domain.repository.ReminderSettingsRepository
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class ReminderSettingsRepositoryImpl @Inject constructor(
    private val dataSource: ReminderPreferencesDataSource
) : ReminderSettingsRepository {

    override fun observeSettings(): Flow<ReminderSettings> =
        combine(
            dataSource.observeEnabled(),
            dataSource.observeThresholds(),
        ) { enabled, rawThresholds ->
            ReminderSettings(
                enabled = enabled,
                thresholds = rawThresholds.mapNotNull { value ->
                    ExpiryThreshold.entries.find { it.name == value }
                }.toSet()
            )
        }

    override suspend fun setEnabled(enabled: Boolean): Result<Unit, DomainError> {
        return when (val result = dataSource.setEnabled(enabled)) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    override suspend fun setThresholds(thresholds: Set<ExpiryThreshold>): Result<Unit, DomainError> {
        return when (val result = dataSource.setThresholds(thresholds.map { it.name }.toSet())) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }
}
