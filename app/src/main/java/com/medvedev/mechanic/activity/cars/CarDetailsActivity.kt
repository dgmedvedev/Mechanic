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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idCar = intent.getStringExtra(ID_CAR)
        val user: Car? = SingletonCar.getCarById(idCar).also {
            if (it == null) {
                showToast(resources.getText(R.string.id_not_found))
                this.finish()
            }
        }

        user?.run {
            binding.brandTextView.text = brand
            binding.modelTextView.text = model
            binding.yearProductionTextView.text = yearProduction.toString()
            binding.stateNumberTextView.text = stateNumber
            binding.bodyNumberTextView.text = bodyNumber
            binding.engineDisplacementTextView.text = engineDisplacement
            binding.fuelTypeTextView.text = fuelType
            binding.allowableWeightTextView.text = allowableWeight
            binding.technicalPassportTextView.text = technicalPassport
            binding.checkupTextView.text = checkup
            binding.insuranceTextView.text = insurance
            binding.hullInsuranceTextView.text = hullInsurance
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

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(context: Context, idCar: String): Intent {
            val intent = Intent(context, CarDetailsActivity::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }
}