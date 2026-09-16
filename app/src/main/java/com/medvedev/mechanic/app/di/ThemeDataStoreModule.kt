package com.medvedev.mechanic.app.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.medvedev.mechanic.data.preferences.ThemePreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore(name = "theme_settings")

@Module
@InstallIn(SingletonComponent::class)
object ThemeDataStoreModule {

    @Provides
    @Singleton
    @ThemePreferences
    fun provideThemeDataStore(
        @ApplicationContext context: Context
    ): DataStore<Preferences> = context.themeDataStore
}
