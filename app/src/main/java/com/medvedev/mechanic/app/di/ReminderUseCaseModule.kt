package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.repository.ReminderSettingsRepository
import com.medvedev.mechanic.domain.usecase.expiry.GetUpcomingExpirationsUseCase
import com.medvedev.mechanic.domain.usecase.expiry.ObserveReminderSettingsUseCase
import com.medvedev.mechanic.domain.usecase.expiry.SetReminderThresholdsUseCase
import com.medvedev.mechanic.domain.usecase.expiry.SetRemindersEnabledUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ReminderUseCaseModule {

    @Provides
    fun provideGetUpcomingExpirationsUseCase(): GetUpcomingExpirationsUseCase =
        GetUpcomingExpirationsUseCase()

    @Provides
    fun provideObserveReminderSettingsUseCase(
        repository: ReminderSettingsRepository
    ): ObserveReminderSettingsUseCase = ObserveReminderSettingsUseCase(repository)

    @Provides
    fun provideSetRemindersEnabledUseCase(
        repository: ReminderSettingsRepository,
        scheduler: ExpiryReminderScheduler,
    ): SetRemindersEnabledUseCase = SetRemindersEnabledUseCase(repository, scheduler)

    @Provides
    fun provideSetReminderThresholdsUseCase(
        repository: ReminderSettingsRepository,
        scheduler: ExpiryReminderScheduler,
    ): SetReminderThresholdsUseCase = SetReminderThresholdsUseCase(repository, scheduler)
}
