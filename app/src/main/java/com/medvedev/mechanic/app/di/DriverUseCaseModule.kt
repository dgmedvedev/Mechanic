package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.domain.repository.DriverRepository
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
        repository: DriverRepository
    ): DeleteDriverUseCase = DeleteDriverUseCase(repository)

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
        repository: DriverRepository
    ): InsertDriverUseCase = InsertDriverUseCase(repository)
}
