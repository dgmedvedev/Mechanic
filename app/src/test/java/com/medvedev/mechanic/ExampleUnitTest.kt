package com.medvedev.mechanic

import com.medvedev.mechanic.activity.cars.Car
import com.medvedev.mechanic.activity.cars.CarEditActivity
import com.medvedev.mechanic.activity.cars.SingletonCar
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun listFromJson() {
        val testCarListBefore = mutableListOf(
            Car(
                "1", "TestCar1", "", "", 2019, "", "",
                "", "", "", "", "",
                "", "", "", "",
                "", "",
                ""
            ),
            Car(
                "2", "TestCar2", "", "", 2018, "", "",
                "", "", "", "", "",
                "", "", "", "",
                "", "",
                ""
            )
        )
        val testCarListString = SingletonCar.listToJson(testCarListBefore)

        val testCarListAfter = SingletonCar.listFromJson(testCarListString)

        assertEquals("TestCar2", testCarListAfter[1].brand)
    }

    @Test
    fun getCarById() {
        SingletonCar.getListCar().add(
            Car(
                "3", "TestCar3", "", "", 2017, "", "",
                "", "", "", "", "",
                "", "", "", "",
                "", "",
                ""
            )
        )
        val car = SingletonCar.getCarById("3")

        assertEquals("TestCar3", car?.brand)
    }

    @Test
    fun filter() {
        SingletonCar.getListCar().add(
            Car(
                "4", "TestCar4", "", "", 2016,
                "4444 TEST-3", "", "", "",
                "", "", "", "", "",
                "", "", "",
                "", ""
            )
        )
        SingletonCar.getListCar().add(
            Car(
                "5", "TestCar5", "", "", 2015,
                "0440 TEST-3", "", "", "",
                "", "", "", "", "",
                "", "", "",
                "", ""
            )
        )
        SingletonCar.getListCar().add(
            Car(
                "6", "TestCar6", "", "", 2014,
                "4004 OO-4", "", "", "",
                "", "", "", "", "",
                "", "", "",
                "", ""
            )
        )

        val testCarList = SingletonCar.filter("TEST-3")

        assertEquals(2, testCarList.size)
    }
}