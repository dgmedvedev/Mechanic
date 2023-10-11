package com.medvedev.presentation.activities.fuel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.medvedev.mechanic.R
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.activities.cars.SingletonCar
import com.medvedev.mechanic.databinding.ActivityDetailsFuelCarBinding

class CarFuelDetailsActivity : Activity() {

    private val binding by lazy {
        ActivityDetailsFuelCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idCar = intent.getStringExtra(ID_CAR)
        val car: Car? = SingletonCar.getCarById(idCar).also {
            if (it == null) {
                showToast(resources.getText(R.string.id_not_found))
                finish()
            }
        }

        car?.let {
            initCar(it)
            setListeners(idCar)
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

    private fun setListeners(idCar: String?) {
        binding.edit.setOnClickListener {
            launchCarFuelEditActivity(idCar)
            finish()
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchCarFuelEditActivity(idCar: String?) {
        val intent = CarFuelEditActivity.getIntent(this, idCar)
        startActivity(intent)
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