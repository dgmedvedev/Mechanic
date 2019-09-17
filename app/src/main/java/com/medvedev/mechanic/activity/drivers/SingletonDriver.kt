package com.medvedev.mechanic.activity.drivers

import com.google.gson.Gson
import java.util.*

object SingletonDriver {

    private var listDriver: MutableList<Driver> = mutableListOf()

    fun getListDriver(): MutableList<Driver> {
        return listDriver
    }

    fun getFillListDriver(): MutableList<Driver> {
        return mutableListOf(
            Driver(
                "1", "Иван", "Иванов", "Иванович", "https://clck.ru/Gx4Nd",
                "01.01.1980", "AA 12345", "01.01.2019", "10.01.2019"
            ),
            Driver(
                "2", "Петр", "Петров", "Петрович", "https://clck.ru/Gx4Nd",
                "02.02.1990", "BB 12345", "02.02.2019", "20.02.2019"
            ),
            Driver(
                "3", "Сидр", "Сидоров", "Сидорович", "https://clck.ru/Gx4Nd",
                "03.03.2000", "CC 12345", "03.03.2019", "30.03.2019"
            )
        )
    }

    fun filter(search: String): List<Driver> {
        return listDriver.filter { it.surname.toUpperCase().contains(search.toUpperCase()) }
    }

    fun getDriverById(id: String?): Driver? {
        return listDriver.find { it.id == id }
    }

    fun setListDrivers(list: MutableList<Driver>) {
        this.listDriver = list
    }

    fun listToJson(): String {
        return Gson().toJson(listDriver)
    }

    fun listFromJson(listJson: String): MutableList<Driver> {
        val arraysListFromJson = stringToArray(listJson, Array<Driver>::class.java)
        val listFromJson = mutableListOf<Driver>()

        for (arrayCars in arraysListFromJson) {
            for (car in arrayCars)
                listFromJson.add(car)
        }
        return listFromJson
    }

    private fun <T> stringToArray(string: String, clazz: Class<Array<T>>): MutableList<Array<T>> {
        val arr = Gson().fromJson(string, clazz)
        return Arrays.asList(arr)
    }
}