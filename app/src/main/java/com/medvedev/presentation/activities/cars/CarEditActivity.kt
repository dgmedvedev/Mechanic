package com.medvedev.presentation.activities.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityEditCarBinding
import com.medvedev.presentation.CarViewModel
import com.medvedev.presentation.pojo.Car

class CarEditActivity : AppCompatActivity() {

    private var idCar: String? = null

    private val pattern = Patterns.WEB_URL

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private val binding by lazy {
        ActivityEditCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idCar = intent.getStringExtra(ID_CAR)

        val car: Car? = SingletonCar.getCarById(idCar)

        car?.let {
            initCar(it)
        }

        setListeners(car)
    }

    private fun initCar(car: Car) {
        with(binding) {
            brandEditText.setText(car.brand)
            modelEditText.setText(car.model)
            yearProductionEditText.setText(car.yearProduction.toString())
            stateNumberEditText.setText(car.stateNumber)
            bodyNumberEditText.setText(car.bodyNumber)
            engineDisplacementEditText.setText(car.engineDisplacement)
            fuelTypeEditText.setText(car.fuelType)
            allowableWeightEditText.setText(car.allowableWeight)
            technicalPassportEditText.setText(car.technicalPassport)
            checkupEditText.setText(car.checkup)
            insuranceEditText.setText(car.insurance)
            hullInsuranceEditText.setText(car.hullInsurance)
        }
    }

    private fun addCar(car: Car?) {
        var id = System.currentTimeMillis().toString()
        val brand = binding.brandEditText.text.toString()
        val model = binding.modelEditText.text.toString()

        val linearFCR = car?.linearFuelConsumptionRate ?: ""
        val summerInCityFCR = car?.summerInCityFuelConsumptionRate ?: ""
        val summerOutCityFCR = car?.summerOutCityFuelConsumptionRate ?: ""
        val winterInCityFCR = car?.winterInCityFuelConsumptionRate ?: ""
        val winterOutCityFCR = car?.winterOutCityFuelConsumptionRate ?: ""

        val stateNumber = binding.stateNumberEditText.text.toString()
        val bodyNumber = binding.bodyNumberEditText.text.toString()
        val engineDisplacement = binding.engineDisplacementEditText.text.toString()
        val fuelType = binding.fuelTypeEditText.text.toString()
        val allowableWeight = binding.allowableWeightEditText.text.toString()
        val technicalPassport = binding.technicalPassportEditText.text.toString()
        val checkup = binding.checkupEditText.text.toString()
        val insurance = binding.insuranceEditText.text.toString()
        val hullInsurance = binding.hullInsuranceEditText.text.toString()

        // imageUrl в разработке
        //var imageUrl = car?.imageUrl ?: ""
        var imageUrl = getString(R.string.image_url)
        if (BuildConfig.DEBUG) {
            imageUrl = getString(R.string.image_url)
        }

        try {
            val yearProduction = binding.yearProductionEditText.text.toString().toInt()

            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            if (idCar != null) {
                car?.let {
                    id = it.id
                    SingletonCar.deleteCar(it)
                }
            }

            SingletonCar.addCar(
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
            addCar(car)
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