package com.medvedev.mechanic.data.notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.medvedev.mechanic.R
import com.medvedev.mechanic.app.MainActivity
import com.medvedev.mechanic.domain.model.ExpiryEntityType
import com.medvedev.mechanic.domain.model.ExpiryEvent
import com.medvedev.mechanic.domain.model.ExpiryField
import com.medvedev.mechanic.domain.model.ExpiryThreshold
import com.medvedev.mechanic.presentation.navigation.ExpiryNotificationIntents
import com.medvedev.mechanic.presentation.navigation.Routes
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExpiryNotificationPublisher @Inject constructor(
    @param:ApplicationContext private val context: Context,
) {

    fun show(events: List<ExpiryEvent>): Boolean {
        if (events.isEmpty()) {
            cancel()
            return true
        }
        val manager = NotificationManagerCompat.from(context)
        if (!manager.areNotificationsEnabled()) {
            return false
        }
        ExpiryNotificationChannels.ensure(context)
        cancel()
        val groups = events.groupBy { it.entityType to it.entityId }
        val grouped = groups.size > 1
        return try {
            if (grouped) {
                manager.notify(
                    TAG,
                    ExpiryNotificationIntents.NOTIFICATION_ID,
                    summaryNotification(events),
                )
            }
            groups.values.forEach { entityEvents ->
                val event = entityEvents.first()
                manager.notify(
                    TAG,
                    notificationId(event),
                    entityNotification(entityEvents, grouped),
                )
            }
            true
        } catch (_: SecurityException) {
            false
        }
    }

    fun cancel() {
        val system = context.getSystemService(NotificationManager::class.java) ?: return
        system.activeNotifications
            .filter { status ->
                status.tag == TAG ||
                        (status.tag == null && status.id == ExpiryNotificationIntents.NOTIFICATION_ID)
            }
            .forEach { status ->
                if (status.tag != null) {
                    system.cancel(status.tag, status.id)
                } else {
                    system.cancel(status.id)
                }
            }
    }

    private fun entityNotification(events: List<ExpiryEvent>, grouped: Boolean): Notification {
        val first = events.first()
        val several = events.size > 1
        val builder = NotificationCompat.Builder(context, ExpiryNotificationIntents.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(first.subjectName)
            .setContentText(if (several) documentsTitle(events.size) else documentLine(first))
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(contentIntent(first))
        if (several) {
            builder.setStyle(inboxStyle(events))
        }
        if (grouped) {
            builder
                .setGroup(GROUP_KEY)
                .setGroupAlertBehavior(NotificationCompat.GROUP_ALERT_SUMMARY)
        }
        return builder.build()
    }

    private fun summaryNotification(events: List<ExpiryEvent>): Notification {
        val title = documentsTitle(events.size)
        return NotificationCompat.Builder(context, ExpiryNotificationIntents.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(title)
            .setAutoCancel(true)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setGroup(GROUP_KEY)
            .setGroupSummary(true)
            .setContentIntent(summaryIntent(events))
            .build()
    }

    private fun documentsTitle(count: Int): String =
        context.resources.getQuantityString(
            R.plurals.expiry_notification_title_many,
            count,
            count,
        )

    private fun inboxStyle(events: List<ExpiryEvent>): NotificationCompat.InboxStyle {
        val style = NotificationCompat.InboxStyle()
        events.forEach { event ->
            style.addLine(documentLine(event))
        }
        return style
    }

    private fun documentLine(event: ExpiryEvent): String {
        val field = context.getString(event.field.toLabelRes())
        val whenText = when (event.threshold) {
            ExpiryThreshold.EXPIRED -> if (event.daysUntil == 0L) {
                context.getString(R.string.expiry_when_today)
            } else {
                context.getString(R.string.expiry_when_overdue)
            }

            ExpiryThreshold.DAYS_1 -> context.getString(R.string.expiry_when_in_one_day)
            else -> context.getString(R.string.expiry_when_in_days, event.daysUntil)
        }
        return context.getString(R.string.expiry_notification_line, field, whenText)
    }

    private fun ExpiryField.toLabelRes(): Int = when (this) {
        ExpiryField.CHECKUP -> R.string.checkup
        ExpiryField.INSURANCE -> R.string.insurance
        ExpiryField.HULL_INSURANCE -> R.string.hull_insurance
        ExpiryField.DRIVING_LICENSE -> R.string.driving_license_validity
        ExpiryField.MEDICAL_CERTIFICATE -> R.string.medical_certificate_validity
    }

    private fun contentIntent(event: ExpiryEvent): PendingIntent {
        val tab = if (event.entityType == ExpiryEntityType.DRIVER) {
            Routes.DRIVERS
        } else {
            Routes.CARS
        }
        return pendingIntent(
            tab = tab,
            entityId = event.entityId,
            requestCode = notificationId(event),
        )
    }

    private fun summaryIntent(events: List<ExpiryEvent>): PendingIntent {
        val tab = if (events.all { it.entityType == ExpiryEntityType.DRIVER }) {
            Routes.DRIVERS
        } else {
            Routes.CARS
        }
        return pendingIntent(
            tab = tab,
            entityId = null,
            requestCode = ExpiryNotificationIntents.NOTIFICATION_ID,
        )
    }

    private fun pendingIntent(
        tab: String,
        entityId: String?,
        requestCode: Int,
    ): PendingIntent {
        val intent = Intent(context, MainActivity::class.java).apply {
            action = ExpiryNotificationIntents.ACTION
            flags = Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra(ExpiryNotificationIntents.EXTRA_TAB, tab)
            if (entityId != null) {
                putExtra(ExpiryNotificationIntents.EXTRA_ENTITY_ID, entityId)
            }
        }
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        return PendingIntent.getActivity(context, requestCode, intent, flags)
    }

    private fun notificationId(event: ExpiryEvent): Int {
        val id = (event.entityType.name + ":" + event.entityId).hashCode()
        return if (id == ExpiryNotificationIntents.NOTIFICATION_ID) id + 1 else id
    }

    private companion object {
        const val TAG = "expiry_reminders"
        const val GROUP_KEY = "expiry_reminders_group"
    }
}
