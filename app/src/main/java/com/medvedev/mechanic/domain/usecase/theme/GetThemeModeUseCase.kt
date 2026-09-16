package com.medvedev.mechanic.domain.usecase.theme

import com.medvedev.mechanic.domain.model.ThemeMode
import com.medvedev.mechanic.domain.repository.ThemeRepository
import kotlinx.coroutines.flow.Flow

class GetThemeModeUseCase(private val repository: ThemeRepository) {
    operator fun invoke(): Flow<ThemeMode> = repository.observeThemeMode()
}
