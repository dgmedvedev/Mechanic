package com.medvedev.mechanic.data.preferences

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface ReminderPreferencesDataSource {

    fun observeEnabled(): Flow<Boolean>

    fun observeThresholds(): Flow<Set<String>>

    suspend fun setEnabled(enabled: Boolean): Result<Unit, DataError>

    suspend fun setThresholds(values: Set<String>): Result<Unit, DataError>
}
