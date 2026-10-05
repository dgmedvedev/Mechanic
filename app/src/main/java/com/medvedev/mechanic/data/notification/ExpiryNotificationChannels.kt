package com.medvedev.mechanic.data.notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.medvedev.mechanic.R
import com.medvedev.mechanic.presentation.navigation.ExpiryNotificationIntents

object ExpiryNotificationChannels {

    fun ensure(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(NotificationManager::class.java) ?: return
        val channel = NotificationChannel(
            ExpiryNotificationIntents.CHANNEL_ID,
            context.getString(R.string.expiry_channel_name),
            NotificationManager.IMPORTANCE_HIGH,
        ).apply {
            description = context.getString(R.string.expiry_channel_description)
        }
        manager.createNotificationChannel(channel)
    }
}
