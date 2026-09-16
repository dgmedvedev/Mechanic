package com.medvedev.mechanic.data.preferences

import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface ThemePreferencesDataSource {

    fun observeThemeMode(): Flow<String>

    suspend fun setThemeMode(value: String): Result<Unit, DataError>
}
