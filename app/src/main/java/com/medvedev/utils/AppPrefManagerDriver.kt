package com.medvedev.utils

import android.content.Context
import android.content.SharedPreferences

class AppPrefManagerDriver(context: Context) {
    private val sharedPrefsNameDriver = "SHARED_PREFS_NAME_DRIVER"

    private val textKeyDriver = "TEXT_KEY_DRIVER"

    private val sharedPrefsDriver: SharedPreferences = context
        .getSharedPreferences(sharedPrefsNameDriver, Context.MODE_PRIVATE)

    fun saveUserText(text: String) {
        sharedPrefsDriver
            .edit()
            .putString(textKeyDriver, text)
            .apply()
    }

    fun getUserText(): String {
        return sharedPrefsDriver.getString(textKeyDriver, "") ?: ""
    }
}