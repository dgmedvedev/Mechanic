package com.medvedev.mechanic.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toData
import com.medvedev.mechanic.domain.model.ExpiryThreshold
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderPreferencesDataSourceImpl @Inject constructor(
    @param:ReminderPreferences private val dataStore: DataStore<Preferences>
) : ReminderPreferencesDataSource {

    override fun observeEnabled(): Flow<Boolean> =
        dataStore.data
            .map { preferences -> preferences[ENABLED_KEY] ?: DEFAULT_ENABLED }
            .catch { emit(DEFAULT_ENABLED) }

    override fun observeThresholds(): Flow<Set<String>> =
        dataStore.data
            .map { preferences -> preferences[THRESHOLDS_KEY] ?: DEFAULT_THRESHOLDS }
            .catch { emit(DEFAULT_THRESHOLDS) }

    override suspend fun setEnabled(enabled: Boolean): Result<Unit, DataError> {
        return try {
            dataStore.edit { preferences -> preferences[ENABLED_KEY] = enabled }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    override suspend fun setThresholds(values: Set<String>): Result<Unit, DataError> {
        return try {
            dataStore.edit { preferences -> preferences[THRESHOLDS_KEY] = values }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    private companion object {
        const val DEFAULT_ENABLED = false
        val DEFAULT_THRESHOLDS = ExpiryThreshold.entries.map { it.name }.toSet()
        val ENABLED_KEY = booleanPreferencesKey("reminders_enabled")
        val THRESHOLDS_KEY = stringSetPreferencesKey("reminder_thresholds")
    }
}
