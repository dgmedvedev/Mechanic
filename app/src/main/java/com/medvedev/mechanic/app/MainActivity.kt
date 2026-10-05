package com.medvedev.mechanic.app

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.medvedev.mechanic.presentation.MechanicApp
import com.medvedev.mechanic.presentation.navigation.ExpiryNotificationIntents
import com.medvedev.mechanic.presentation.navigation.ExpiryOpenRequest
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var expiryOpen by mutableStateOf<ExpiryOpenRequest?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        disableNavigationBarContrast()
        expiryOpen = intent.toExpiryOpen()
        setContent {
            MechanicApp(
                expiryOpen = expiryOpen,
                onExpiryOpenConsumed = { expiryOpen = null },
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        expiryOpen = intent.toExpiryOpen()
    }

    private fun disableNavigationBarContrast() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }
    }
}

private fun Intent.toExpiryOpen(): ExpiryOpenRequest? {
    if (action != ExpiryNotificationIntents.ACTION) return null
    val tab = getStringExtra(ExpiryNotificationIntents.EXTRA_TAB) ?: return null
    val entityId = getStringExtra(ExpiryNotificationIntents.EXTRA_ENTITY_ID)
    return ExpiryOpenRequest(tab = tab, entityId = entityId)
}
