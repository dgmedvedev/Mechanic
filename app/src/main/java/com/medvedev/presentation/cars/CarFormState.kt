package com.medvedev.presentation.cars

import com.medvedev.domain.model.Car
import com.medvedev.presentation.common.UiState

data class CarFormState(
    val brand: String = "",
    val model: String = "",
    val yearProduction: String = "",
    val stateNumber: String = "",
    val bodyNumber: String = "",
    val engineDisplacement: String = "",
    val fuelType: String = "",
    val allowableWeight: String = "",
    val technicalPassport: String = "",
    val checkup: String = "",
    val insurance: String = "",
    val hullInsurance: String = "",
    val linearFcr: String = "",
    val summerInCityFcr: String = "",
    val summerOutCityFcr: String = "",
    val winterInCityFcr: String = "",
    val winterOutCityFcr: String = "",
) {
    companion object {
        fun fromCar(car: Car) = CarFormState(
            brand = car.brand,
            model = car.model,
            yearProduction = car.yearProduction.toString(),
            stateNumber = car.stateNumber,
            bodyNumber = car.bodyNumber,
            engineDisplacement = car.engineDisplacement,
            fuelType = car.fuelType,
            allowableWeight = car.allowableWeight,
            technicalPassport = car.technicalPassport,
            checkup = car.checkup,
            insurance = car.insurance,
            hullInsurance = car.hullInsurance,
            linearFcr = car.linearFuelConsumptionRate,
            summerInCityFcr = car.summerInCityFuelConsumptionRate,
            summerOutCityFcr = car.summerOutCityFuelConsumptionRate,
            winterInCityFcr = car.winterInCityFuelConsumptionRate,
            winterOutCityFcr = car.winterOutCityFuelConsumptionRate,
        )
    }
}

data class CarEditUiState(
    val form: CarFormState = CarFormState(),
    val existingCar: Car? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessageRes: Int? = null,
    val saveCompleted: Boolean = false,
) : UiState
