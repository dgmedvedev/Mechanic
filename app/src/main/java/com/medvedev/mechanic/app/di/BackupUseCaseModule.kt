package com.medvedev.mechanic.app.di

import com.medvedev.mechanic.domain.repository.BackupRepository
import com.medvedev.mechanic.domain.usecase.backup.ExportBackupUseCase
import com.medvedev.mechanic.domain.usecase.backup.RestoreBackupUseCase
import com.medvedev.mechanic.domain.usecase.backup.SaveBackupUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object BackupUseCaseModule {

    @Provides
    fun provideExportBackupUseCase(
        repository: BackupRepository,
    ): ExportBackupUseCase = ExportBackupUseCase(repository)

    @Provides
    fun provideSaveBackupUseCase(
        repository: BackupRepository,
    ): SaveBackupUseCase = SaveBackupUseCase(repository)

    @Provides
    fun provideRestoreBackupUseCase(
        repository: BackupRepository,
    ): RestoreBackupUseCase = RestoreBackupUseCase(repository)
}
