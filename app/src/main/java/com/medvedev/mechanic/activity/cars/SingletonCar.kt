package com.medvedev.mechanic.activity.cars

import com.google.gson.Gson
import java.util.*

object SingletonCar {

    private var listCar: MutableList<Car> = mutableListOf()

    fun getListCar(): MutableList<Car> {
        return listCar
    }

    fun getFillListCar(): MutableList<Car> {
        return mutableListOf(
            Car(
                "1", "Volkswagen", "Passat", "https://clck.ru/Gx4Nd", 2017,
                "7777 MI-7", "ZW111222333", "1600", "бензин",
                "1400", "", "", "", "",
                "6.6", "", "",
                "", ""
            ),
            Car(
                "2", "BMW", "X5", "https://clck.ru/Gx4Nd", 2018,
                "8888 BO-7", "ZW222333444", "3200", "бензин",
                "2000", "", "", "", "",
                "7.7", "", "",
                "", ""
            ),
            Car(
                "3", "Toyota", "Camry", "https://clck.ru/Gx4Nd", 2019,
                "0007 OO-7", "ZW333444555", "1800", "бензин",
                "1600", "", "", "", "",
                "8.8", "", "",
                "", ""
            )
        )
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

    fun listToJson(): String {
        return Gson().toJson(getListCar())
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