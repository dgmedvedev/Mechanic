package com.medvedev.mechanic.domain.repository

interface ExpiryReminderScheduler {

    fun enqueuePeriodic()

    fun enqueueImmediate()
}
