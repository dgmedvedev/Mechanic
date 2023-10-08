package com.medvedev.utils

import android.content.Context
import android.content.SharedPreferences

class AppPrefManagerDriver(context: Context) {
    private val sharedPrefsDrivers = "SHARED_PREFS_DRIVERS"

    private val textKeyDriver = "TEXT_KEY_DRIVER"

    private val sharedPrefs: SharedPreferences = context
        .getSharedPreferences(sharedPrefsDrivers, Context.MODE_PRIVATE)

    fun saveUserText(text: String) {
        sharedPrefs
            .edit()
            .putString(textKeyDriver, text)
            .apply()
    }

    fun getUserText(): String {
        return sharedPrefs.getString(textKeyDriver, "") ?: ""
    }
}