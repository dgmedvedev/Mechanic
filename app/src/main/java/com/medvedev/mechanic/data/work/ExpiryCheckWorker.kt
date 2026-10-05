package com.medvedev.mechanic.data.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.medvedev.mechanic.domain.model.toReminderKey
import com.medvedev.mechanic.domain.repository.ExpiryReminderRepository
import com.medvedev.mechanic.domain.usecase.car.GetCarsUseCase
import com.medvedev.mechanic.domain.usecase.driver.GetDriversUseCase
import com.medvedev.mechanic.domain.usecase.expiry.GetUpcomingExpirationsUseCase
import com.medvedev.mechanic.domain.usecase.expiry.ObserveReminderSettingsUseCase
import com.medvedev.mechanic.data.notification.ExpiryNotificationPublisher
import com.medvedev.mechanic.domain.result.Result as DomainResult
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import androidx.hilt.work.HiltWorker
import kotlinx.coroutines.flow.first
import java.time.LocalDate

@HiltWorker
class ExpiryCheckWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted params: WorkerParameters,
    private val getCarsUseCase: GetCarsUseCase,
    private val getDriversUseCase: GetDriversUseCase,
    private val observeReminderSettingsUseCase: ObserveReminderSettingsUseCase,
    private val getUpcomingExpirationsUseCase: GetUpcomingExpirationsUseCase,
    private val expiryReminderRepository: ExpiryReminderRepository,
    private val publisher: ExpiryNotificationPublisher,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val settings = observeReminderSettingsUseCase().first()
        if (!settings.enabled) {
            publisher.cancel()
            return Result.success()
        }
        val today = LocalDate.now()
        val cars = getCarsUseCase().first()
        val drivers = getDriversUseCase().first()
        val upcoming = getUpcomingExpirationsUseCase(
            cars = cars,
            drivers = drivers,
            today = today,
            thresholds = settings.thresholds,
        )
        val shown = when (val result = expiryReminderRepository.getShownKeys()) {
            is DomainResult.Success -> result.data
            is DomainResult.Error -> return Result.retry()
        }
        val pending = upcoming.filter { it.toReminderKey() !in shown }
        if (pending.isEmpty()) {
            if (upcoming.isEmpty()) publisher.cancel()
            return Result.success()
        }
        if (!publisher.show(pending)) {
            return Result.success()
        }
        return when (
            expiryReminderRepository.markShown(
                keys = pending.map { it.toReminderKey() },
                shownOnEpochDay = today.toEpochDay(),
            )
        ) {
            is DomainResult.Success -> Result.success()
            is DomainResult.Error -> Result.retry()
        }
    }
}
