package com.medvedev.mechanic.presentation.preview

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.medvedev.mechanic.domain.model.Car
import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.presentation.theme.MechanicTheme

internal val PreviewCar = Car(
    id = "1",
    brand = "Audi",
    model = "TT",
    imageUrl = "",
    yearProduction = 2010,
    stateNumber = "1234 AA-7",
    vin = "WVWZZZ3BZWE123456",
    engineDisplacement = "2.0",
    fuelType = "Бензин",
    allowableWeight = "1800",
    technicalPassport = "AB 123456",
    checkup = "01.06.2027",
    insurance = "01.01.2027",
    hullInsurance = "",
    linearFuelConsumptionRate = "8.5",
    summerInCityFuelConsumptionRate = "10.2",
    summerOutCityFuelConsumptionRate = "7.4",
    winterInCityFuelConsumptionRate = "11.0",
    winterOutCityFuelConsumptionRate = "8.1",
)

internal val PreviewCars = listOf(
    PreviewCar,
    PreviewCar.copy(
        id = "2",
        brand = "BMW",
        model = "X5",
        yearProduction = 2018,
        stateNumber = "4004 OO-4",
    ),
    PreviewCar.copy(
        id = "3",
        brand = "Toyota",
        model = "Camry",
        yearProduction = 2015,
        stateNumber = "",
    ),
)

internal val PreviewDriver = Driver(
    id = "1",
    name = "Иван",
    surname = "Иванов",
    middleName = "Иванович",
    imageUrl = "",
    birthday = "12.03.1988",
    drivingLicenseNumber = "AA1234567",
    drivingLicenseValidity = "01.01.2030",
    medicalCertificateValidity = "01.06.2027",
)

internal val PreviewDrivers = listOf(
    PreviewDriver,
    PreviewDriver.copy(
        id = "2",
        name = "Пётр",
        surname = "Петров",
        middleName = "Петрович",
        drivingLicenseNumber = "BB1234567",
    ),
)

@Composable
internal fun PreviewMechanicTheme(content: @Composable () -> Unit) {
    MechanicTheme {
        Surface(color = MaterialTheme.colorScheme.background, content = content)
    }
}
