package com.medvedev.presentation.ui.activities.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.medvedev.mechanic.R
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.cars.CarEditFragment

class CarEditActivity : AppCompatActivity(), OnEditingFinishedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_edit)

        if (savedInstanceState == null) {
            launchFragment()
        }
    }

    override fun onEditingFinished() {
        finish()
    }

    private fun launchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.car_container, CarEditFragment.getInstanceCarAdd())
            .commit()
    }

    companion object {
        fun getIntentAddCar(context: Context): Intent {
            return Intent(context, CarEditActivity::class.java)
        }
    }
}