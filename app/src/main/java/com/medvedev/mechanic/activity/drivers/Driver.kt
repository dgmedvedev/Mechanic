package com.medvedev.mechanic.activity.drivers

class Driver (
    val id: String,
    val name: String,
    val surname: String,
    val middleName: String,

    val imageUrl: String, // в разработке
    val birthday: String,

    val drivingLicenseNumber: String,
    val drivingLicenseValidity: String,

    val medicalCertificateValidity: String
)