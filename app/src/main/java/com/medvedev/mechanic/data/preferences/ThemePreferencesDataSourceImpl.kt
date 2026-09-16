package com.medvedev.mechanic.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.medvedev.mechanic.data.error.DataError
import com.medvedev.mechanic.data.error.toData
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemePreferencesDataSourceImpl @Inject constructor(
    @param:ThemePreferences private val dataStore: DataStore<Preferences>,
) : ThemePreferencesDataSource {

    override fun observeThemeMode(): Flow<String> =
        dataStore.data
            .map { preferences ->
                preferences[THEME_MODE_KEY] ?: DEFAULT_THEME_MODE
            }
            .catch { emit(DEFAULT_THEME_MODE) }

    override suspend fun setThemeMode(value: String): Result<Unit, DataError> {
        return try {
            dataStore.edit { preferences ->
                preferences[THEME_MODE_KEY] = value
            }
            Result.Success(Unit)
        } catch (e: Exception) {
            Result.Error(e.toData())
        }
    }

    private companion object {
        const val DEFAULT_THEME_MODE = "system"
        val THEME_MODE_KEY = stringPreferencesKey("theme_mode")
    }
}
