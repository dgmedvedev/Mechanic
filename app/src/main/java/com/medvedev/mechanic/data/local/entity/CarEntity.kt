package com.medvedev.mechanic.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cars")
data class CarEntity(
    @PrimaryKey
    val id: String,
    val brand: String,
    val model: String,
    val yearProduction: Int,

    val stateNumber: String,
    val vin: String,
    val engineDisplacement: String,
    val fuelType: String,
    val allowableWeight: String,
    val technicalPassport: String,
    val checkup: String,
    val insurance: String,
    val hullInsurance: String,

    val linearFuelConsumptionRate: String,
    val summerInCityFuelConsumptionRate: String,
    val summerOutCityFuelConsumptionRate: String,
    val winterInCityFuelConsumptionRate: String,
    val winterOutCityFuelConsumptionRate: String
)