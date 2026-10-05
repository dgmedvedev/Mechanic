package com.medvedev.mechanic.presentation.navigation

data class ExpiryOpenRequest(
    val tab: String,
    val entityId: String?,
)

object ExpiryNotificationIntents {
    const val ACTION = "com.medvedev.mechanic.OPEN_EXPIRY"
    const val EXTRA_TAB = "expiry_tab"
    const val EXTRA_ENTITY_ID = "expiry_entity_id"
    const val NOTIFICATION_ID = 1001
    const val CHANNEL_ID = "expiry_reminders"
}
