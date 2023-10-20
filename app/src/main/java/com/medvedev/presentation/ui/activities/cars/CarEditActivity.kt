package com.medvedev.presentation.ui.activities.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityCarEditBinding
import com.medvedev.presentation.viewmodel.CarViewModel
import com.medvedev.presentation.pojo.Car
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CarEditActivity : AppCompatActivity() {

    private var idCar: String? = null

    private val pattern = Patterns.WEB_URL

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private val binding by lazy {
        ActivityCarEditBinding.inflate(layoutInflater)
    }

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idCar = intent.getStringExtra(ID_CAR)

        lifecycleScope.launch {
            var car: Car? = null
            idCar?.let { id ->
                car = carViewModel.getCarById(id)
                car?.let {
                    initCar(it)
                }
            }
            setListeners(car)
        }
    }

    private fun initCar(car: Car) {
        with(binding) {
            etBrand.setText(car.brand)
            etModel.setText(car.model)
            etYearProduction.setText(car.yearProduction.toString())
            etStateNumber.setText(car.stateNumber)
            etBodyNumber.setText(car.bodyNumber)
            etEngineDisplacement.setText(car.engineDisplacement)
            etFuelType.setText(car.fuelType)
            etAllowableWeight.setText(car.allowableWeight)
            etTechnicalPassport.setText(car.technicalPassport)
            etCheckup.setText(car.checkup)
            etInsurance.setText(car.insurance)
            etHullInsurance.setText(car.hullInsurance)
        }
    }

    private suspend fun addCar(car: Car?) {
        var id = System.currentTimeMillis().toString()
        val brand = binding.etBrand.text.toString()
        val model = binding.etModel.text.toString()

        val linearFCR = car?.linearFuelConsumptionRate ?: ""
        val summerInCityFCR = car?.summerInCityFuelConsumptionRate ?: ""
        val summerOutCityFCR = car?.summerOutCityFuelConsumptionRate ?: ""
        val winterInCityFCR = car?.winterInCityFuelConsumptionRate ?: ""
        val winterOutCityFCR = car?.winterOutCityFuelConsumptionRate ?: ""

        val stateNumber = binding.etStateNumber.text.toString()
        val bodyNumber = binding.etBodyNumber.text.toString()
        val engineDisplacement = binding.etEngineDisplacement.text.toString()
        val fuelType = binding.etFuelType.text.toString()
        val allowableWeight = binding.etAllowableWeight.text.toString()
        val technicalPassport = binding.etTechnicalPassport.text.toString()
        val checkup = binding.etCheckup.text.toString()
        val insurance = binding.etInsurance.text.toString()
        val hullInsurance = binding.etHullInsurance.text.toString()

        // imageUrl в разработке
        //var imageUrl = car?.imageUrl ?: ""
        var imageUrl = getString(R.string.image_url)
        if (BuildConfig.DEBUG) {
            imageUrl = getString(R.string.image_url)
        }

        try {
            val yearProduction = binding.etYearProduction.text.toString().toInt()

            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            if (idCar != null) {
                car?.let {
                    id = it.id
                    carViewModel.deleteCar(it)
                }
            }

            carViewModel.insertCar(
                Car(
                    id,
                    if (brand == "") resources.getString(R.string.brand) else brand,
                    if (model == "") resources.getString(R.string.model) else model,
                    imageUrl,
                    yearProduction,
                    stateNumber,
                    bodyNumber,
                    engineDisplacement,
                    fuelType,
                    allowableWeight,
                    technicalPassport,
                    checkup,
                    insurance,
                    hullInsurance,
                    linearFCR,
                    summerInCityFCR,
                    summerOutCityFCR,
                    winterInCityFCR,
                    winterOutCityFCR
                )
            )
            finish()
        } catch (nfe: NumberFormatException) {
            showToast(resources.getText(R.string.enter_year_production))
        } catch (hfe: HttpFormatException) {
            showToast(resources.getText(R.string.not_valid_url))
        }
    }

    private fun setListeners(car: Car?) {
        binding.save.setOnClickListener {
            lifecycleScope.launch {
                addCar(car)
            }
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(
            context: Context,
            idCar: String? = System.currentTimeMillis().toString()
        ): Intent {
            val intent = Intent(context, CarEditActivity::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }

    internal inner class HttpFormatException : Exception()
}