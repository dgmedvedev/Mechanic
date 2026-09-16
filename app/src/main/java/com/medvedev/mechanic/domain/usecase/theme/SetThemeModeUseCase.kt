package com.medvedev.mechanic.domain.usecase.theme

import com.medvedev.mechanic.domain.error.DomainError
import com.medvedev.mechanic.domain.model.ThemeMode
import com.medvedev.mechanic.domain.repository.ThemeRepository
import com.medvedev.mechanic.domain.result.Result

class SetThemeModeUseCase(private val repository: ThemeRepository) {
    suspend operator fun invoke(mode: ThemeMode): Result<Unit, DomainError> =
        repository.setThemeMode(mode)
}
