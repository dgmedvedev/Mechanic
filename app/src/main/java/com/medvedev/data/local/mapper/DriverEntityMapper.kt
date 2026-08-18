package com.medvedev.data.local.mapper

import com.medvedev.data.local.entity.DriverEntity
import com.medvedev.domain.model.Driver

fun Driver.toEntity() = DriverEntity(
    id = id,
    name = name,
    surname = surname,
    middleName = middleName,
    imageUrl = imageUrl,
    birthday = birthday,
    drivingLicenseNumber = drivingLicenseNumber,
    drivingLicenseValidity = drivingLicenseValidity,
    medicalCertificateValidity = medicalCertificateValidity
)

fun DriverEntity.toDomain() = Driver(
    id = id,
    name = name,
    surname = surname,
    middleName = middleName,
    imageUrl = imageUrl,
    birthday = birthday,
    drivingLicenseNumber = drivingLicenseNumber,
    drivingLicenseValidity = drivingLicenseValidity,
    medicalCertificateValidity = medicalCertificateValidity
)

fun List<DriverEntity>.toDomain(): List<Driver> =
    map { it.toDomain() }
