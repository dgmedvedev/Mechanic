package com.medvedev.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "driver_items")
data class DriverItemDbModel(
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