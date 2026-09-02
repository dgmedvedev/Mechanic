package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.data.docs.DocumentFileDataSource
import com.medvedev.mechanic.data.docs.DocumentFileDataSourceImpl
import com.medvedev.mechanic.data.docs.DocumentRemoteDataSource
import com.medvedev.mechanic.data.docs.DocumentRemoteDataSourceImpl
import com.medvedev.mechanic.data.docs.search.PdfSearchIndexDataSource
import com.medvedev.mechanic.data.docs.search.PdfSearchIndexDataSourceImpl
import com.medvedev.mechanic.data.local.datasource.LocalDataSource
import com.medvedev.mechanic.data.local.datasource.LocalDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    abstract fun bindLocalDataSource(
        impl: LocalDataSourceImpl
    ): LocalDataSource

    @Binds
    @Singleton
    abstract fun bindDocumentFileDataSource(
        impl: DocumentFileDataSourceImpl
    ): DocumentFileDataSource

    @Binds
    @Singleton
    abstract fun bindDocumentRemoteDataSource(
        impl: DocumentRemoteDataSourceImpl
    ): DocumentRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindPdfSearchIndexDataSource(
        impl: PdfSearchIndexDataSourceImpl
    ): PdfSearchIndexDataSource
}
