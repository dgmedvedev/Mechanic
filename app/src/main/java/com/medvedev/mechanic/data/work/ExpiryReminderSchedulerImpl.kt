package com.medvedev.mechanic.data.work

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.medvedev.mechanic.domain.repository.ExpiryReminderScheduler
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpiryReminderSchedulerImpl @Inject constructor(
    @param:ApplicationContext private val context: Context
) : ExpiryReminderScheduler {

    override fun enqueuePeriodic() {
        val request = PeriodicWorkRequestBuilder<ExpiryCheckWorker>(1, TimeUnit.DAYS)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            PERIODIC_WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    override fun enqueueImmediate() {
        val request = OneTimeWorkRequestBuilder<ExpiryCheckWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            ONE_SHOT_WORK_NAME,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    private companion object {
        const val PERIODIC_WORK_NAME = "expiry_check_periodic"
        const val ONE_SHOT_WORK_NAME = "expiry_check_once"
    }
}
