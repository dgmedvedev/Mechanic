package com.medvedev.app.di

import com.medvedev.data.repository.CarRepositoryImpl
import com.medvedev.data.repository.DriverRepositoryImpl
import com.medvedev.domain.repository.CarRepository
import com.medvedev.domain.repository.DriverRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindCarRepository(
        impl: CarRepositoryImpl
    ): CarRepository

    @Binds
    @Singleton
    abstract fun bindDriverRepository(
        impl: DriverRepositoryImpl
    ): DriverRepository
}
