package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.data.repository.CarRepositoryImpl
import com.medvedev.mechanic.data.repository.DocumentRepositoryImpl
import com.medvedev.mechanic.data.repository.DriverRepositoryImpl
import com.medvedev.mechanic.domain.repository.CarRepository
import com.medvedev.mechanic.domain.repository.DocumentRepository
import com.medvedev.mechanic.domain.repository.DriverRepository
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

    @Binds
    @Singleton
    abstract fun bindDocumentRepository(
        impl: DocumentRepositoryImpl
    ): DocumentRepository
}
