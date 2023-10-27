package com.medvedev.mechanic

import com.medvedev.domain.pojo.Car
import com.medvedev.presentation.activities.cars.SingletonCar
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
        val testCarListString = SingletonCar.listToJson()

        val testCarListAfter = SingletonCar.listFromJson(testCarListString)

        assertEquals("TestCar2", testCarListAfter[1].brand)
    }

    @Test
    fun filter() {

        val testCarList = SingletonCar.filter("TEST-3")

        assertEquals(2, testCarList.size)
    }
}