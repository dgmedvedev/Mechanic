package com.medvedev.mechanic.data.local.mapper

import com.medvedev.mechanic.data.local.entity.DriverEntity
import com.medvedev.mechanic.domain.model.Driver

fun Driver.toEntity() = DriverEntity(
    id = id,
    name = name,
    surname = surname,
    middleName = middleName,
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
    birthday = birthday,
    drivingLicenseNumber = drivingLicenseNumber,
    drivingLicenseValidity = drivingLicenseValidity,
    medicalCertificateValidity = medicalCertificateValidity
)

fun List<DriverEntity>.toDomain(): List<Driver> =
    map { it.toDomain() }
