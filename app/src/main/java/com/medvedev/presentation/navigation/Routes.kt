package com.medvedev.presentation.navigation

object Routes {
    const val MAIN = "main"

    const val CARS = "cars"
    const val CAR_DETAILS = "cars/{carId}"
    const val CAR_ADD = "cars/add"
    const val CAR_EDIT = "cars/{carId}/edit"

    const val DRIVERS = "drivers"
    const val DRIVER_DETAILS = "drivers/{driverId}"
    const val DRIVER_ADD = "drivers/add"
    const val DRIVER_EDIT = "drivers/{driverId}/edit"

    const val FUEL = "fuel"
    const val FUEL_DETAILS = "fuel/{carId}"
    const val FUEL_EDIT = "fuel/{carId}/edit"

    const val DOCS = "docs"
    const val DOCS_NORMS = "docs/norms"
    const val DOCS_RESOLUTION470 = "docs/resolution470"

    fun carDetails(carId: String) = "cars/$carId"
    fun carEdit(carId: String) = "cars/$carId/edit"
    fun driverDetails(driverId: String) = "drivers/$driverId"
    fun driverEdit(driverId: String) = "drivers/$driverId/edit"
    fun fuelDetails(carId: String) = "fuel/$carId"
    fun fuelEdit(carId: String) = "fuel/$carId/edit"
}
