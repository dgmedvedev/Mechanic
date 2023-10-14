package com.medvedev.presentation.activities.drivers

import com.google.gson.Gson
import com.medvedev.presentation.pojo.Driver
import java.util.Locale

object SingletonDriver {

    private var listDriver: MutableList<Driver> = mutableListOf()

    fun getListDriver(): List<Driver> {
        return listDriver.toList()
    }

    fun addDriver(driver: Driver) {
        listDriver.add(driver)
    }

    fun deleteDriver(driver: Driver) {
        listDriver.remove(driver)
    }

    fun filter(search: String): List<Driver> {
        return listDriver.filter {
            it.surname.uppercase(Locale.getDefault())
                .contains(search.uppercase(Locale.getDefault()))
        }
    }

    fun getDriverById(id: String?): Driver? {
        return listDriver.find { it.id == id }
    }

    fun setListDrivers(list: MutableList<Driver>) {
        listDriver = list
    }

    fun listToJson(): String {
        return Gson().toJson(listDriver)
    }

    fun listFromJson(listJson: String): MutableList<Driver> {
        val arraysListFromJson = stringToArray(listJson, Array<Driver>::class.java)
        val listFromJson = mutableListOf<Driver>()

        for (arrayDrivers in arraysListFromJson) {
            for (driver in arrayDrivers)
                listFromJson.add(driver)
        }
        return listFromJson
    }

    private fun <T> stringToArray(string: String, clazz: Class<Array<T>>): List<Array<T>> {
        val arr = Gson().fromJson(string, clazz)
        return listOf(arr)
    }
}