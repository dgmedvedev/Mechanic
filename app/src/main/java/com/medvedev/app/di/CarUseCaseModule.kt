package com.medvedev.app.di

import com.medvedev.domain.repository.CarRepository
import com.medvedev.domain.usecase.car.DeleteCarUseCase
import com.medvedev.domain.usecase.car.GetCarByIdUseCase
import com.medvedev.domain.usecase.car.GetCarsUseCase
import com.medvedev.domain.usecase.car.GetStateNumbersUseCase
import com.medvedev.domain.usecase.car.InsertCarUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CarUseCaseModule {

    @Provides
    fun provideDeleteCarUseCase(
        repository: CarRepository
    ): DeleteCarUseCase = DeleteCarUseCase(repository)

    @Provides
    fun provideGetCarByIdUseCase(
        repository: CarRepository
    ): GetCarByIdUseCase = GetCarByIdUseCase(repository)

    @Provides
    fun provideGetCarsUseCase(
        repository: CarRepository
    ): GetCarsUseCase = GetCarsUseCase(repository)

    @Provides
    fun provideGetStateNumbersUseCase(
        repository: CarRepository
    ): GetStateNumbersUseCase = GetStateNumbersUseCase(repository)

    @Provides
    fun provideInsertCarUseCase(
        repository: CarRepository
    ): InsertCarUseCase = InsertCarUseCase(repository)
}
