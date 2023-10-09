package com.medvedev.mechanic.activity.cars

import com.google.gson.Gson
import java.util.*

object SingletonCar {

    private var listCar: MutableList<Car> = mutableListOf()

//    private val _listCarLiveData = MutableLiveData<List<Car>>()
//    val listCarLiveData: LiveData<List<Car>>
//        get() = _listCarLiveData

    fun getListCar(): List<Car> {
//        _listCarLiveData.value = listCar
        return listCar.toList()
    }

    fun addCar(car: Car) {
        listCar.add(car)
    }

    fun deleteCar(car: Car) {
        listCar.remove(car)
    }
//
//    fun editCar(car: Car){
//        val oldElement = getCarById(car.id)
//        deleteCar(oldElement)
//    }

    fun filter(search: String): List<Car> {
        val list = listCar.filter { it.stateNumber.toUpperCase().contains(search.toUpperCase()) }
//        _listCarLiveData.value = list
        return list
    }

    fun getCarById(id: String?): Car? {
        return listCar.find { it.id == id }
    }

    fun setListCars(list: MutableList<Car>) {
//        _listCarLiveData.value = list
        listCar = list
    }

    fun listToJson(): String {
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

    private fun <T> stringToArray(string: String, clazz: Class<Array<T>>): List<Array<T>> {
        val arr = Gson().fromJson(string, clazz)
        return listOf(arr)
    }
}