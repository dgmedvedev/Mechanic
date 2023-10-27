package com.medvedev.presentation.ui.activities.fuel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.medvedev.mechanic.R
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.cars.CarDetailsFragment

class CarFuelDetailsActivity : AppCompatActivity(), OnEditingFinishedListener {

    private var idCar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_fuel_details)

        parseIntent()
        if (savedInstanceState == null) {
            idCar?.let {
                launchFragment(CarDetailsFragment.getInstance(it))
            }
        }
    }

    override fun onEditingFinished() {
        finish()
    }

    private fun parseIntent() {
        idCar = intent.getStringExtra(ID_CAR)

        if (idCar == null) {
            showToast(getString(R.string.id_null))
            finish()
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.car_fuel_details_container, fragment)
            .commit()
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(context: Context, idCar: String): Intent {
            val intent = Intent(context, CarFuelDetailsActivity::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }
}