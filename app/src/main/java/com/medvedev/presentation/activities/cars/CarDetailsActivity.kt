package com.medvedev.presentation.activities.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityDetailsCarBinding
import com.medvedev.presentation.CarViewModel
import com.medvedev.presentation.pojo.Car
import kotlinx.coroutines.launch

class CarDetailsActivity : AppCompatActivity() {

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private val binding by lazy {
        ActivityDetailsCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            var car: Car? = null
            val idCar = intent.getStringExtra(ID_CAR)

            idCar?.let {
                car = carViewModel.getCarById(it)
            }

            if (car == null) {
                showToast(resources.getText(R.string.id_not_found))
                finish()
            }

            car?.let { it ->
                initCar(it)
                setListeners(it, idCar)
            }
        }
    }

    private fun initCar(car: Car) {
        with(binding) {
            brandTextView.text = car.brand
            modelTextView.text = car.model
            yearProductionTextView.text = car.yearProduction.toString()
            stateNumberTextView.text = car.stateNumber
            bodyNumberTextView.text = car.bodyNumber
            engineDisplacementTextView.text = car.engineDisplacement
            fuelTypeTextView.text = car.fuelType
            allowableWeightTextView.text = car.allowableWeight
            technicalPassportTextView.text = car.technicalPassport
            checkupTextView.text = car.checkup
            insuranceTextView.text = car.insurance
            hullInsuranceTextView.text = car.hullInsurance
        }
    }

    private fun setListeners(car: Car, idCar: String?) {
        binding.delete.setOnClickListener {
            lifecycleScope.launch {
                carViewModel.deleteCar(car)
                finish()
            }
        }

        binding.edit.setOnClickListener {
            launchCarEditActivity(idCar)
            finish()
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchCarEditActivity(idCar: String?) {
        val intent = CarEditActivity.getIntent(this, idCar)
        startActivity(intent)
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