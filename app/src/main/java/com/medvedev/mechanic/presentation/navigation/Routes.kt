package com.medvedev.mechanic.presentation.navigation

object Routes {
    const val CARS = "cars"
    const val CAR_DETAILS = "cars/{carId}"
    const val CAR_ADD = "cars/add"

    const val DRIVERS = "drivers"
    const val DRIVER_DETAILS = "drivers/{driverId}"
    const val DRIVER_ADD = "drivers/add"

    const val DOCS = "docs"
    const val DOC_VIEW = "docs/{documentId}"

    fun carDetails(carId: String) = "cars/$carId"
    fun driverDetails(driverId: String) = "drivers/$driverId"
    fun docView(documentId: String) = "docs/$documentId"
}
