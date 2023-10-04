package com.medvedev.mechanic.activity.fuel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.Car
import com.medvedev.mechanic.activity.cars.SingletonCar
import com.medvedev.mechanic.databinding.ActivityDetailsFuelCarBinding

class CarFuelDetailsActivity : Activity() {

    private val binding by lazy {
        ActivityDetailsFuelCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idCar = intent.getStringExtra(ID_CAR)
        val car: Car? = SingletonCar.getCarById(idCar)
        if (car == null) {
            showToast(resources.getText(R.string.id_not_found))
            this.finish()
        }

        car?.let {
            initCar(it)
        }

        binding.edit.setOnClickListener {
            startActivity(CarFuelEditActivity.getIntent(this@CarFuelDetailsActivity, idCar))
            this.finish()
        }
    }

    private fun initCar(car: Car) {
        with(binding) {
            brandTextView.text = car.brand
            modelTextView.text = car.model
            yearProductionTextView.text = car.yearProduction.toString()
            stateNumberTextView.text = car.stateNumber

            linearFCRTextView.text = car.linearFuelConsumptionRate
            summerInCityTextView.text = car.summerInCityFuelConsumptionRate
            summerOutCityTextView.text = car.summerOutCityFuelConsumptionRate
            winterInCityTextView.text = car.winterInCityFuelConsumptionRate
            winterOutCityTextView.text = car.winterOutCityFuelConsumptionRate
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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