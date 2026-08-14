package com.medvedev.app.di

import com.medvedev.data.local.datasource.LocalDataSource
import com.medvedev.data.local.datasource.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindLocalDataSource(
        impl: LocalDataSourceImpl
    ): LocalDataSource
}
