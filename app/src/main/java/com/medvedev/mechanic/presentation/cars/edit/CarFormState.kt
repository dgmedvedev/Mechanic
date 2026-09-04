package com.medvedev.mechanic.presentation.cars.edit

import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.presentation.common.UiState
import com.medvedev.mechanic.presentation.components.TextInputFilters

data class CarFormState(
    val brand: String = "",
    val model: String = "",
    val yearProduction: String = "",
    val stateNumber: String = "",
    val vin: String = "",
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
    fun normalized(): CarFormState = copy(
        brand = TextInputFilters.capitalizeFirst(brand),
        model = TextInputFilters.capitalizeFirst(model),
        yearProduction = TextInputFilters.year(yearProduction),
        stateNumber = TextInputFilters.uppercase(stateNumber),
        vin = TextInputFilters.uppercase(vin),
        engineDisplacement = TextInputFilters.finalizeDecimal(engineDisplacement),
        allowableWeight = TextInputFilters.finalizeDecimal(allowableWeight),
        technicalPassport = TextInputFilters.uppercase(technicalPassport),
        linearFcr = TextInputFilters.formatFuelRate(linearFcr),
        summerInCityFcr = TextInputFilters.formatFuelRate(summerInCityFcr),
        summerOutCityFcr = TextInputFilters.formatFuelRate(summerOutCityFcr),
        winterInCityFcr = TextInputFilters.formatFuelRate(winterInCityFcr),
        winterOutCityFcr = TextInputFilters.formatFuelRate(winterOutCityFcr),
    )

    fun isBrandValid(): Boolean = brand.isNotBlank()

    fun isModelValid(): Boolean = model.isNotBlank()

    fun isYearValid(): Boolean {
        val year = yearProduction.toIntOrNull()
        return year != null && year in YEAR_RANGE
    }

    fun isValid(): Boolean = isBrandValid() && isModelValid() && isYearValid()

    companion object {
        private val YEAR_RANGE = 1900..2100

        fun fromCar(car: Car) = CarFormState(
            brand = car.brand,
            model = car.model,
            yearProduction = car.yearProduction.toString(),
            stateNumber = car.stateNumber,
            vin = car.vin,
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
        ).normalized()
    }
}

data class CarEditUiState(
    val form: CarFormState = CarFormState(),
    val existingCar: Car? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessageRes: Int? = null,
    val showFieldErrors: Boolean = false,
    val saveCompleted: Boolean = false,
) : UiState {
    val isDirty: Boolean
        get() {
            val initial = existingCar?.let { CarFormState.fromCar(it) } ?: CarFormState()
            return form != initial
        }
}
