package com.medvedev.mechanic.domain.repository

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ThemeMode
import com.medvedev.mechanic.domain.result.Result
import kotlinx.coroutines.flow.Flow

interface ThemeRepository {

    fun observeThemeMode(): Flow<ThemeMode>

    suspend fun setThemeMode(mode: ThemeMode): Result<Unit, DomainError>
}
