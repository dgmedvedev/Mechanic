package com.medvedev.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "car_items")
data class CarItemDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: String,
    val brand: String,
    val model: String,
    val imageUrl: String, // в разработке
    val yearProduction: Int,

    val stateNumber: String,
    val bodyNumber: String,
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