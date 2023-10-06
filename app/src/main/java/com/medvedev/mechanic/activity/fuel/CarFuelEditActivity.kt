package com.medvedev.mechanic.activity.fuel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.viewbinding.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.activity.cars.Car
import com.medvedev.mechanic.activity.cars.SingletonCar
import com.medvedev.mechanic.databinding.ActivityEditFuelCarBinding

class CarFuelEditActivity : Activity() {

    private var idCar: String? = null

    private val pattern = Patterns.WEB_URL

    private val binding by lazy {
        ActivityEditFuelCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idCar = intent.getStringExtra(ID_CAR)

        val car: Car? = SingletonCar.getCarById(idCar)

        car?.let {
            initCar(it)
        }

        binding.save.setOnClickListener {
            addCar(car)
        }
    }

    private fun initCar(car: Car) {
        with(binding) {
            brandEditText.setText(car.brand)
            modelEditText.setText(car.model)
            yearProductionEditText.setText(car.yearProduction.toString())
            stateNumberEditText.setText(car.stateNumber)

            linearFCREditText.setText(car.linearFuelConsumptionRate)
            summerInCityEditText.setText(car.summerInCityFuelConsumptionRate)
            summerOutCityEditText.setText(car.summerOutCityFuelConsumptionRate)
            winterInCityEditText.setText(car.winterInCityFuelConsumptionRate)
            winterOutCityEditText.setText(car.winterOutCityFuelConsumptionRate)
        }
    }

    private fun addCar(car: Car?) {
        var id = System.currentTimeMillis().toString()
        val brand = binding.brandEditText.text.toString()
        val model = binding.modelEditText.text.toString()
        val stateNumber = binding.stateNumberEditText.text.toString()

        val engineDisplacement = car?.engineDisplacement ?: ""
        val fuelType = car?.fuelType ?: ""
        val bodyNumber = car?.bodyNumber ?: ""
        val allowableWeight = car?.allowableWeight ?: ""
        val technicalPassport = car?.technicalPassport ?: ""
        val checkup = car?.checkup ?: ""
        val insurance = car?.insurance ?: ""
        val hullInsurance = car?.hullInsurance ?: ""

        val linearFCR = binding.linearFCREditText.text.toString()
        val summerInCityFCR = binding.summerInCityEditText.text.toString()
        val summerOutCityFCR = binding.summerOutCityEditText.text.toString()
        val winterInCityFCR = binding.winterInCityEditText.text.toString()
        val winterOutCityFCR = binding.winterOutCityEditText.text.toString()

        // imageUrl в разработке
        //var imageUrl = user?.imageUrl ?: ""
        var imageUrl = getString(R.string.image_url)
        if (BuildConfig.DEBUG) {
            imageUrl = getString(R.string.image_url)
        }

        try {
            val yearProduction = binding.yearProductionEditText.text.toString().toInt()

            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            if (idCar != null) {
                SingletonCar.getListCar().remove(car)
                car?.let {
                    id = it.id
                }
            }
            SingletonCar.getListCar().add(
                Car(
                    id,
                    if (brand == "") resources.getString(R.string.brand) else brand,
                    if (model == "") resources.getString(R.string.model) else model,
                    imageUrl,
                    yearProduction,
                    stateNumber,
                    engineDisplacement,
                    fuelType,
                    bodyNumber,
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
            this.finish()
        } catch (nfe: NumberFormatException) {
            showToast(resources.getText(R.string.enter_year_production))
        } catch (hfe: HttpFormatException) {
            showToast(resources.getText(R.string.not_valid_url))
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
            val intent = Intent(context, CarFuelEditActivity::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }

    internal inner class HttpFormatException : Exception()
}