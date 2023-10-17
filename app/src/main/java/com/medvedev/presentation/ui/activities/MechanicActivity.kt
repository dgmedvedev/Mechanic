package com.medvedev.presentation.ui.activities

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.medvedev.presentation.ui.activities.cars.CarListActivity
import com.medvedev.presentation.ui.activities.docs.NormativeDocsActivity
import com.medvedev.presentation.ui.activities.drivers.DriverListActivity
import com.medvedev.presentation.ui.activities.fuel.CarFuelListActivity
import com.medvedev.mechanic.databinding.ActivityMechanicBinding

class MechanicActivity : Activity() {

    private val binding by lazy {
        ActivityMechanicBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        with(binding) {
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
    }

    private fun start(activity: Activity) {
        startActivity(Intent(this, activity::class.java))
    }
}