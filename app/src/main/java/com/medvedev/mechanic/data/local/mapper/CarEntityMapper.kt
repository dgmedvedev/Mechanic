package com.medvedev.mechanic.data.local.mapper

import com.medvedev.mechanic.data.local.entity.CarEntity
import com.medvedev.mechanic.domain.model.Car

fun Car.toEntity() = CarEntity(
    id = id,
    brand = brand,
    model = model,
    yearProduction = yearProduction,
    stateNumber = stateNumber,
    vin = vin,
    engineDisplacement = engineDisplacement,
    fuelType = fuelType,
    allowableWeight = allowableWeight,
    technicalPassport = technicalPassport,
    checkup = checkup,
    insurance = insurance,
    hullInsurance = hullInsurance,
    linearFuelConsumptionRate = linearFuelConsumptionRate,
    summerInCityFuelConsumptionRate = summerInCityFuelConsumptionRate,
    summerOutCityFuelConsumptionRate = summerOutCityFuelConsumptionRate,
    winterInCityFuelConsumptionRate = winterInCityFuelConsumptionRate,
    winterOutCityFuelConsumptionRate = winterOutCityFuelConsumptionRate
)

fun CarEntity.toDomain() = Car(
    id = id,
    brand = brand,
    model = model,
    yearProduction = yearProduction,
    stateNumber = stateNumber,
    vin = vin,
    engineDisplacement = engineDisplacement,
    fuelType = fuelType,
    allowableWeight = allowableWeight,
    technicalPassport = technicalPassport,
    checkup = checkup,
    insurance = insurance,
    hullInsurance = hullInsurance,
    linearFuelConsumptionRate = linearFuelConsumptionRate,
    summerInCityFuelConsumptionRate = summerInCityFuelConsumptionRate,
    summerOutCityFuelConsumptionRate = summerOutCityFuelConsumptionRate,
    winterInCityFuelConsumptionRate = winterInCityFuelConsumptionRate,
    winterOutCityFuelConsumptionRate = winterOutCityFuelConsumptionRate
)

fun List<CarEntity>.toDomain(): List<Car> =
    map { it.toDomain() }
