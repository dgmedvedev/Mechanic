package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.domain.repository.ThemeRepository
import com.medvedev.mechanic.domain.usecase.theme.GetThemeModeUseCase
import com.medvedev.mechanic.domain.usecase.theme.SetThemeModeUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ThemeUseCaseModule {

    @Provides
    fun provideGetThemeModeUseCase(
        repository: ThemeRepository
    ): GetThemeModeUseCase = GetThemeModeUseCase(repository)

    @Provides
    fun provideSetThemeModeUseCase(
        repository: ThemeRepository
    ): SetThemeModeUseCase = SetThemeModeUseCase(repository)
}
