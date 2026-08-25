package com.medvedev.mechanic.domain.model

data class Driver (
    val id: String,
    val name: String,
    val surname: String,
    val middleName: String,

    val birthday: String,

    val drivingLicenseNumber: String,
    val drivingLicenseValidity: String,

    val medicalCertificateValidity: String
)