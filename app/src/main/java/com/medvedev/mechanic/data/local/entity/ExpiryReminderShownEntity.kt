package com.medvedev.mechanic.data.local.entity

import androidx.room.Entity

@Entity(
    tableName = "expiry_reminder_shown",
    primaryKeys = ["entityType", "entityId", "field", "threshold"],
)
data class ExpiryReminderShownEntity(
    val entityType: String,
    val entityId: String,
    val field: String,
    val threshold: String,
    val shownOnEpochDay: Long,
)
