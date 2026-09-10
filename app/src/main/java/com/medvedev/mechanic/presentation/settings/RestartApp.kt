package com.medvedev.mechanic.presentation.settings

import android.content.Context
import android.content.Intent

fun Context.restartApp() {
    val launchIntent = packageManager.getLaunchIntentForPackage(packageName) ?: return
    val restartIntent = Intent.makeRestartActivityTask(launchIntent.component)
    startActivity(restartIntent)
    Runtime.getRuntime().exit(0)
}
