package com.medvedev.utils

import android.content.Context
import android.content.SharedPreferences

class AppPrefManagerCar(context: Context) {
    private val sharedPrefsCars = "SHARED_PREFS_CARS"

    private val textKeyCar = "TEXT_KEY_CAR"

    private val sharedPrefs: SharedPreferences = context
        .getSharedPreferences(sharedPrefsCars, Context.MODE_PRIVATE)

    fun saveUserText(text: String) {
        sharedPrefs
            .edit()
            .putString(textKeyCar, text)
            .apply()
    }

    fun getUserText(): String {
        return sharedPrefs.getString(textKeyCar, "") ?: ""
    }
}