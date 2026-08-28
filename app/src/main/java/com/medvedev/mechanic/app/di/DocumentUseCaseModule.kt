package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.domain.repository.DocumentRepository
import com.medvedev.mechanic.domain.usecase.document.GetDocumentUseCase
import com.medvedev.mechanic.domain.usecase.document.PrepareDocumentUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DocumentUseCaseModule {

    @Provides
    fun provideGetDocumentUseCase(
        repository: DocumentRepository
    ): GetDocumentUseCase = GetDocumentUseCase(repository)

    @Provides
    fun providePrepareDocumentUseCase(
        repository: DocumentRepository
    ): PrepareDocumentUseCase = PrepareDocumentUseCase(repository)
}
