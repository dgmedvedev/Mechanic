package com.medvedev.data.mapper

import com.medvedev.data.database.CarDbModel
import com.medvedev.data.database.DriverDbModel
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.pojo.Driver

class AppMapper {

    fun mapCarToCarDbModel(car: Car) = CarDbModel(
        id = car.id,
        brand = car.brand,
        model = car.model,
        imageUrl = car.imageUrl,
        yearProduction = car.yearProduction,
        stateNumber = car.stateNumber,
        bodyNumber = car.bodyNumber,
        engineDisplacement = car.engineDisplacement,
        fuelType = car.fuelType,
        allowableWeight = car.allowableWeight,
        technicalPassport = car.technicalPassport,
        checkup = car.checkup,
        insurance = car.insurance,
        hullInsurance = car.hullInsurance,
        linearFuelConsumptionRate = car.linearFuelConsumptionRate,
        summerInCityFuelConsumptionRate = car.summerInCityFuelConsumptionRate,
        summerOutCityFuelConsumptionRate = car.summerOutCityFuelConsumptionRate,
        winterInCityFuelConsumptionRate = car.winterInCityFuelConsumptionRate,
        winterOutCityFuelConsumptionRate = car.winterOutCityFuelConsumptionRate
    )

    fun mapCarDbModelToCar(carDbModel: CarDbModel) = Car(
        id = carDbModel.id,
        brand = carDbModel.brand,
        model = carDbModel.model,
        imageUrl = carDbModel.imageUrl,
        yearProduction = carDbModel.yearProduction,
        stateNumber = carDbModel.stateNumber,
        bodyNumber = carDbModel.bodyNumber,
        engineDisplacement = carDbModel.engineDisplacement,
        fuelType = carDbModel.fuelType,
        allowableWeight = carDbModel.allowableWeight,
        technicalPassport = carDbModel.technicalPassport,
        checkup = carDbModel.checkup,
        insurance = carDbModel.insurance,
        hullInsurance = carDbModel.hullInsurance,
        linearFuelConsumptionRate = carDbModel.linearFuelConsumptionRate,
        summerInCityFuelConsumptionRate = carDbModel.summerInCityFuelConsumptionRate,
        summerOutCityFuelConsumptionRate = carDbModel.summerOutCityFuelConsumptionRate,
        winterInCityFuelConsumptionRate = carDbModel.winterInCityFuelConsumptionRate,
        winterOutCityFuelConsumptionRate = carDbModel.winterOutCityFuelConsumptionRate
    )

    fun mapDriverToDriverDbModel(driver: Driver) = DriverDbModel(
        id = driver.id,
        name = driver.name,
        surname = driver.surname,
        middleName = driver.middleName,
        imageUrl = driver.imageUrl,
        birthday = driver.birthday,
        drivingLicenseNumber = driver.drivingLicenseNumber,
        drivingLicenseValidity = driver.drivingLicenseValidity,
        medicalCertificateValidity = driver.medicalCertificateValidity
    )

    fun mapDriverDbModelToDriver(driverDbModel: DriverDbModel) = Driver(
        id = driverDbModel.id,
        name = driverDbModel.name,
        surname = driverDbModel.surname,
        middleName = driverDbModel.middleName,
        imageUrl = driverDbModel.imageUrl,
        birthday = driverDbModel.birthday,
        drivingLicenseNumber = driverDbModel.drivingLicenseNumber,
        drivingLicenseValidity = driverDbModel.drivingLicenseValidity,
        medicalCertificateValidity = driverDbModel.medicalCertificateValidity
    )

    fun mapCarsListDbModelToCarsList(listDbModel: List<CarDbModel>): List<Car> =
        listDbModel.map { mapCarDbModelToCar(it) }

    fun mapDriversListDbModelToDriversList(listDbModel: List<DriverDbModel>): List<Driver> =
        listDbModel.map { mapDriverDbModelToDriver(it) }
}