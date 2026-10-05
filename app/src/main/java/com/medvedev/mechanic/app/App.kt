package com.medvedev.mechanic.app

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.medvedev.mechanic.data.notification.ExpiryNotificationChannels
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    @Inject
    lateinit var expiryReminderScheduler: ExpiryReminderScheduler

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()
        ExpiryNotificationChannels.ensure(this)
        expiryReminderScheduler.enqueuePeriodic()
        expiryReminderScheduler.enqueueImmediate()
    }
}
