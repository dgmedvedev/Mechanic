package com.medvedev.mechanic.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.CarListActivity
import com.medvedev.mechanic.activity.drivers.DriverListActivity
import com.medvedev.mechanic.activity.fuel.CarFuelListActivity

class MechanicActivity : Activity() {

    private lateinit var carsButton: Button
    private lateinit var driversButton: Button
    private lateinit var fuelButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanic)

        carsButton = findViewById(R.id.carsButton)
        driversButton = findViewById(R.id.driversButton)
        fuelButton = findViewById(R.id.fuelButton)

        carsButton.setOnClickListener {
            startCarListActivity()
        }
        driversButton.setOnClickListener {
            startDriverListActivity()
        }
        fuelButton.setOnClickListener {
            startCarFuelListActivity()
        }
    }

    private fun startCarListActivity() {
        val intent = Intent(this, CarListActivity::class.java)
        startActivity(intent)
    }

    private fun startDriverListActivity() {
        val intent = Intent(this, DriverListActivity::class.java)
        startActivity(intent)
    }
    private fun startCarFuelListActivity() {
        val intent = Intent(this, CarFuelListActivity::class.java)
        startActivity(intent)
    }
}