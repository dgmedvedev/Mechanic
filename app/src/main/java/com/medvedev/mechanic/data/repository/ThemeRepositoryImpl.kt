package com.medvedev.mechanic.data.repository

import com.medvedev.mechanic.data.error.toDomain
import com.medvedev.mechanic.data.preferences.ThemePreferencesDataSource
import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ThemeMode
import com.medvedev.mechanic.domain.repository.ThemeRepository
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ThemeRepositoryImpl @Inject constructor(
    private val dataSource: ThemePreferencesDataSource,
) : ThemeRepository {

    override fun observeThemeMode(): Flow<ThemeMode> =
        dataSource.observeThemeMode().map { it.toThemeMode() }

    override suspend fun setThemeMode(mode: ThemeMode): Result<Unit, DomainError> {
        return when (val result = dataSource.setThemeMode(mode.toStorageValue())) {
            is Result.Success -> Result.Success(result.data)
            is Result.Error -> Result.Error(result.error.toDomain())
        }
    }

    private fun String.toThemeMode(): ThemeMode = when (this) {
        STORAGE_LIGHT -> ThemeMode.LIGHT
        STORAGE_DARK -> ThemeMode.DARK
        else -> ThemeMode.SYSTEM
    }

    private fun ThemeMode.toStorageValue(): String = when (this) {
        ThemeMode.SYSTEM -> STORAGE_SYSTEM
        ThemeMode.LIGHT -> STORAGE_LIGHT
        ThemeMode.DARK -> STORAGE_DARK
    }

    private companion object {
        const val STORAGE_SYSTEM = "system"
        const val STORAGE_LIGHT = "light"
        const val STORAGE_DARK = "dark"
    }
}
