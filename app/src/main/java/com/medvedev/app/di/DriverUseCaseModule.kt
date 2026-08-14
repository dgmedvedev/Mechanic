package com.medvedev.app.di

import com.medvedev.domain.repository.DriverRepository
import com.medvedev.domain.usecase.driver.DeleteDriverUseCase
import com.medvedev.domain.usecase.driver.GetDriverByIdUseCase
import com.medvedev.domain.usecase.driver.GetDriversUseCase
import com.medvedev.domain.usecase.driver.GetSurnamesUseCase
import com.medvedev.domain.usecase.driver.InsertDriverUseCase
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
    fun provideGetSurnamesUseCase(
        repository: DriverRepository
    ): GetSurnamesUseCase = GetSurnamesUseCase(repository)

    @Provides
    fun provideInsertDriverUseCase(
        repository: DriverRepository
    ): InsertDriverUseCase = InsertDriverUseCase(repository)
}
