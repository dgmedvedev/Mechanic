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
        setContentView(binding.root)

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
            binding.brandTextView.text = user.brand
            binding.modelTextView.text = user.model
            binding.yearProductionTextView.text = user.yearProduction.toString()
            binding.stateNumberTextView.text = user.stateNumber

            binding.linearFCRTextView.text = user.linearFuelConsumptionRate
            binding.summerInCityTextView.text = user.summerInCityFuelConsumptionRate
            binding.summerOutCityTextView.text = user.summerOutCityFuelConsumptionRate
            binding.winterInCityTextView.text = user.winterInCityFuelConsumptionRate
            binding.winterOutCityTextView.text = user.winterOutCityFuelConsumptionRate
        }

        binding.edit.setOnClickListener {
            startActivity(CarFuelEditActivity.getIntent(this@CarFuelDetailsActivity, idCar))
            this.finish()
        }
    }
}