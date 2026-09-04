package com.medvedev.mechanic.presentation.drivers.edit

import com.medvedev.mechanic.domain.model.Driver
import com.medvedev.mechanic.presentation.common.UiState
import com.medvedev.mechanic.presentation.components.TextInputFilters

data class DriverFormState(
    val name: String = "",
    val surname: String = "",
    val middleName: String = "",
    val birthday: String = "",
    val drivingLicenseNumber: String = "",
    val drivingLicenseValidity: String = "",
    val medicalCertificateValidity: String = "",
) {
    fun normalized(): DriverFormState = copy(
        name = TextInputFilters.capitalizeFirst(name),
        surname = TextInputFilters.capitalizeFirst(surname),
        middleName = TextInputFilters.capitalizeFirst(middleName),
        drivingLicenseNumber = TextInputFilters.uppercase(drivingLicenseNumber),
    )

    companion object {
        fun fromDriver(driver: Driver) = DriverFormState(
            name = driver.name,
            surname = driver.surname,
            middleName = driver.middleName,
            birthday = driver.birthday,
            drivingLicenseNumber = driver.drivingLicenseNumber,
            drivingLicenseValidity = driver.drivingLicenseValidity,
            medicalCertificateValidity = driver.medicalCertificateValidity,
        ).normalized()
    }
}

data class DriverEditUiState(
    val form: DriverFormState = DriverFormState(),
    val existingDriver: Driver? = null,
    val isLoading: Boolean = true,
    val isSaving: Boolean = false,
    val errorMessageRes: Int? = null,
    val saveCompleted: Boolean = false,
) : UiState
