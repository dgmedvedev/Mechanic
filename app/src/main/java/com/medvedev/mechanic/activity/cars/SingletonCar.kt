package com.medvedev.mechanic.activity.cars

import com.google.gson.Gson
import java.util.*

object SingletonCar {

    private var listCar: MutableList<Car> = mutableListOf()

    fun getListCar(): MutableList<Car> {
        return listCar
    }

    fun filter(search: String): List<Car> {
        return listCar.filter { it.stateNumber.toUpperCase().contains(search.toUpperCase()) }
    }

    fun getCarById(id: String?): Car? {
        return listCar.find { it.id == id }
    }

    fun setListCars(list: MutableList<Car>) {
        this.listCar = list
    }

    fun listToJson(listCar: MutableList<Car>): String {
        return Gson().toJson(listCar)
    }

    fun listFromJson(listJson: String): MutableList<Car> {
        val arraysListFromJson = stringToArray(listJson, Array<Car>::class.java)
        val listFromJson = mutableListOf<Car>()

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