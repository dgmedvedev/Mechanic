package com.medvedev.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "drivers")
data class DriverDbModel(
    @PrimaryKey
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