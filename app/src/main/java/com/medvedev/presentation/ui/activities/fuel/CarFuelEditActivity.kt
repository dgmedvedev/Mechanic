package com.medvedev.presentation.ui.activities.fuel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.medvedev.mechanic.R
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.fuel.CarFuelEditFragment

class CarFuelEditActivity : AppCompatActivity(), OnEditingFinishedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_fuel_edit)

        if (savedInstanceState == null) {
            launchFragment()
        }
    }

    override fun onEditingFinished() {
        finish()
    }

    private fun launchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.car_container, CarFuelEditFragment.getInstanceCarAdd())
            .commit()
    }

    companion object {
        fun getIntentAddCar(context: Context): Intent {
            return Intent(context, CarFuelEditActivity::class.java)
        }
    }
}