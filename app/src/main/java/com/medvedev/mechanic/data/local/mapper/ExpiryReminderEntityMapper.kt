package com.medvedev.mechanic.data.local.mapper

import com.medvedev.mechanic.data.local.entity.ExpiryReminderShownEntity
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.model.ExpiryField
import com.medvedev.mechanic.domain.model.ExpiryReminderKey
import com.medvedev.mechanic.domain.model.ExpiryThreshold

fun ExpiryReminderShownEntity.toDomainOrNull(): ExpiryReminderKey? {
    val type = ExpiryEntityType.entries.find { it.name == entityType } ?: return null
    val expiryField = ExpiryField.entries.find { it.name == field } ?: return null
    val expiryThreshold = ExpiryThreshold.entries.find { it.name == threshold } ?: return null
    return ExpiryReminderKey(type, entityId, expiryField, expiryThreshold)
}

fun ExpiryReminderKey.toEntity(shownOnEpochDay: Long) = ExpiryReminderShownEntity(
    entityType = entityType.name,
    entityId = entityId,
    field = field.name,
    threshold = threshold.name,
    shownOnEpochDay = shownOnEpochDay,
)
