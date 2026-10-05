package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.domain.repository.DriverRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderRepository
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import com.medvedev.mechanic.domain.usecase.driver.DeleteDriverUseCase
import com.medvedev.mechanic.domain.usecase.driver.GetDriverByIdUseCase
import com.medvedev.mechanic.domain.usecase.driver.GetDriversUseCase
import com.medvedev.mechanic.domain.usecase.driver.InsertDriverUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DriverUseCaseModule {

    @Provides
    fun provideDeleteDriverUseCase(
        repository: DriverRepository,
        expiryReminderRepository: ExpiryReminderRepository,
        scheduler: ExpiryReminderScheduler,
    ): DeleteDriverUseCase = DeleteDriverUseCase(repository, expiryReminderRepository, scheduler)

    @Provides
    fun provideGetDriverByIdUseCase(
        repository: DriverRepository
    ): GetDriverByIdUseCase = GetDriverByIdUseCase(repository)

    @Provides
    fun provideGetDriversUseCase(
        repository: DriverRepository
    ): GetDriversUseCase = GetDriversUseCase(repository)

    @Provides
    fun provideInsertDriverUseCase(
        repository: DriverRepository,
        expiryReminderRepository: ExpiryReminderRepository,
        scheduler: ExpiryReminderScheduler,
    ): InsertDriverUseCase = InsertDriverUseCase(repository, expiryReminderRepository, scheduler)
}
