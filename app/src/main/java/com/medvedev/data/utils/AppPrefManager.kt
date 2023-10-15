package com.medvedev.data.utils

import android.content.Context
import android.content.SharedPreferences

class AppPrefManager(context: Context) {
    private val labelCars = "LABEL_CARS"
    private val labelDrivers = "LABEL_DRIVERS"

    private val keyCars = "KEY_CARS"
    private val keyDrivers = "KEY_DRIVERS"

    private val sharedPrefsCars: SharedPreferences = context
        .getSharedPreferences(labelCars, Context.MODE_PRIVATE)

    private val sharedPrefsDrivers: SharedPreferences = context
        .getSharedPreferences(labelDrivers, Context.MODE_PRIVATE)

    fun saveSharedPrefsCars(text: String) {
        sharedPrefsCars
            .edit()
            .putString(keyCars, text)
            .apply()
    }

    fun saveSharedPrefsDrivers(text: String) {
        sharedPrefsDrivers
            .edit()
            .putString(keyDrivers, text)
            .apply()
    }

    fun getSharedPrefsCars(): String {
        return sharedPrefsCars.getString(keyCars, "") ?: ""
    }

    fun getSharedPrefsDrivers(): String {
        return sharedPrefsDrivers.getString(keyDrivers, "") ?: ""
    }
}