package com.medvedev.mechanic.activity.cars

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityDetailsCarBinding

class CarDetailsActivity : Activity() {

    private val binding by lazy {
        ActivityDetailsCarBinding.inflate(layoutInflater)
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(context: Context, idCar: String): Intent {
            val intent = Intent(context, CarDetailsActivity::class.java)
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
            binding.bodyNumberTextView.text = user.bodyNumber
            binding.engineDisplacementTextView.text = user.engineDisplacement
            binding.fuelTypeTextView.text = user.fuelType
            binding.allowableWeightTextView.text = user.allowableWeight
            binding.technicalPassportTextView.text = user.technicalPassport
            binding.checkupTextView.text = user.checkup
            binding.insuranceTextView.text = user.insurance
            binding.hullInsuranceTextView.text = user.hullInsurance
        }

        binding.delete.setOnClickListener {
            SingletonCar.getListCar().remove(user)
            this.finish()
        }

        binding.edit.setOnClickListener {
            startActivity(CarEditActivity.getIntent(this@CarDetailsActivity, idCar))
            this.finish()
        }
    }
}