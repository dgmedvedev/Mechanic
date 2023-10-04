package com.medvedev.mechanic.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.medvedev.mechanic.activity.cars.CarListActivity
import com.medvedev.mechanic.activity.docs.NormativeDocsActivity
import com.medvedev.mechanic.activity.drivers.DriverListActivity
import com.medvedev.mechanic.activity.fuel.CarFuelListActivity
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