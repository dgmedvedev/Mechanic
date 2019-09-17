package com.medvedev.mechanic.activity.fuel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.Car
import com.medvedev.mechanic.activity.cars.SingletonCar
import kotlinx.android.synthetic.main.activity_details_fuel_car.*

class CarFuelDetailsActivity : Activity() {

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(context: Context, idCar: String): Intent {
            val intent = Intent(context, CarFuelDetailsActivity::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_details_fuel_car)

        val idCar = intent.getStringExtra(ID_CAR)
        val user: Car? = SingletonCar.getCarById(idCar)
        if (user == null) {
            Toast.makeText(
                this,
                resources.getText(R.string.id_not_found),
                Toast.LENGTH_SHORT
            ).show()
            this.finish()
        }

        user?.run {
            brandTextView.text = user.brand
            modelTextView.text = user.model
            yearProductionTextView.text = user.yearProduction.toString()
            stateNumberTextView.text = user.stateNumber

            linearFCRTextView.text = user.linearFuelConsumptionRate
            summerInCityTextView.text = user.summerInCityFuelConsumptionRate
            summerOutCityTextView.text = user.summerOutCityFuelConsumptionRate
            winterInCityTextView.text = user.winterInCityFuelConsumptionRate
            winterOutCityTextView.text = user.winterOutCityFuelConsumptionRate
        }

        edit.setOnClickListener {
            startActivity(CarFuelEditActivity.getIntent(this@CarFuelDetailsActivity, idCar))
            this.finish()
        }
    }
}