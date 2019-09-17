package com.medvedev.mechanic.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.CarListActivity
import com.medvedev.mechanic.activity.docs.NormativeDocsActivity
import com.medvedev.mechanic.activity.drivers.DriverListActivity
import com.medvedev.mechanic.activity.fuel.CarFuelListActivity

class MechanicActivity : Activity() {

    private lateinit var carsButton: Button
    private lateinit var driversButton: Button
    private lateinit var fuelButton: Button
    private lateinit var docsButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mechanic)

        carsButton = findViewById(R.id.carsButton)
        driversButton = findViewById(R.id.driversButton)
        fuelButton = findViewById(R.id.fuelButton)
        docsButton = findViewById(R.id.docsButton)

        carsButton.setOnClickListener {
            start(CarListActivity())
        }
        driversButton.setOnClickListener {
            start(DriverListActivity())
        }
        fuelButton.setOnClickListener {
            start(CarFuelListActivity())
        }
        docsButton.setOnClickListener {
            start(NormativeDocsActivity())
        }
    }

    private fun start(activity: Activity) {
        startActivity(Intent(this, activity::class.java))
    }
}