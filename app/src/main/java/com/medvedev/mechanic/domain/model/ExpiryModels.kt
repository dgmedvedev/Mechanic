package com.medvedev.mechanic.domain.model

import java.time.LocalDate

enum class ExpiryEntityType {
    CAR,
    DRIVER,
}

enum class ExpiryField {
    CHECKUP,
    INSURANCE,
    HULL_INSURANCE,
    DRIVING_LICENSE,
    MEDICAL_CERTIFICATE,
}

enum class ExpiryThreshold {
    DAYS_30,
    DAYS_14,
    DAYS_7,
    DAYS_1,
    EXPIRED,
}

data class ReminderSettings(
    val enabled: Boolean = false,
    val thresholds: Set<ExpiryThreshold> = ExpiryThreshold.entries.toSet(),
)

data class ExpiryEvent(
    val entityType: ExpiryEntityType,
    val entityId: String,
    val subjectName: String,
    val field: ExpiryField,
    val date: LocalDate,
    val daysUntil: Long,
    val threshold: ExpiryThreshold,
)

data class ExpiryReminderKey(
    val entityType: ExpiryEntityType,
    val entityId: String,
    val field: ExpiryField,
    val threshold: ExpiryThreshold,
)

fun ExpiryEvent.toReminderKey(): ExpiryReminderKey =
    ExpiryReminderKey(entityType, entityId, field, threshold)
