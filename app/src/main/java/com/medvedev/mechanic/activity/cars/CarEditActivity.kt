package com.medvedev.mechanic.activity.cars

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityEditCarBinding

class CarEditActivity : Activity() {

    private var idCar: String? = null

    private val pattern = Patterns.WEB_URL

    private val binding by lazy {
        ActivityEditCarBinding.inflate(layoutInflater)
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idCar = intent.getStringExtra(ID_CAR)

        val car: Car? = SingletonCar.getCarById(idCar)

        car?.let {
            binding.brandEditText.setText(car.brand)
            binding.modelEditText.setText(car.model)
            binding.yearProductionEditText.setText(car.yearProduction.toString())
            binding.stateNumberEditText.setText(car.stateNumber)
            binding.bodyNumberEditText.setText(car.bodyNumber)
            binding.engineDisplacementEditText.setText(car.engineDisplacement)
            binding.fuelTypeEditText.setText(car.fuelType)
            binding.allowableWeightEditText.setText(car.allowableWeight)
            binding.technicalPassportEditText.setText(car.technicalPassport)
            binding.checkupEditText.setText(car.checkup)
            binding.insuranceEditText.setText(car.insurance)
            binding.hullInsuranceEditText.setText(car.hullInsurance)
        }

        binding.save.setOnClickListener {
            val id = System.currentTimeMillis().toString()
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
            var imageUrl = "https://clck.ru/Gx4Nd"
            if (BuildConfig.DEBUG) {
                imageUrl = "https://clck.ru/Gx4Nd"
            }

            try {
                val yearProduction = binding.yearProductionEditText.text.toString().toInt()

                if (idCar != null) {
                    SingletonCar.getListCar().remove(car)
                    car?.let {
                        addCar(
                            car.id,
                            brand,
                            model,
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
                    }
                } else {
                    addCar(
                        id, brand, model, imageUrl, yearProduction, stateNumber, bodyNumber,
                        engineDisplacement, fuelType, allowableWeight, technicalPassport, checkup,
                        insurance, hullInsurance, linearFCR, summerInCityFCR, summerOutCityFCR,
                        winterInCityFCR, winterOutCityFCR
                    )
                }
            } catch (nfe: NumberFormatException) {
                Toast.makeText(
                    this,
                    resources.getText(R.string.enter_year_production),
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun addCar(
        id: String, brand: String, model: String, imageUrl: String, yearProduction: Int,
        stateNumber: String, bodyNumber: String, engineDisplacement: String, fuelType: String,
        allowableWeight: String, technicalPassport: String,
        checkup: String, insurance: String, hullInsurance: String,
        linearFCR: String, summerInCityFCR: String, summerOutCityFCR: String,
        winterInCityFCR: String, winterOutCityFCR: String
    ) {
        try {
            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            SingletonCar.getListCar().add(
                Car(
                    id,
                    if (brand == "") {
                        resources.getString(R.string.brand)
                    } else brand,
                    if (model == "") {
                        resources.getString(R.string.model)
                    } else model,
                    imageUrl, yearProduction, stateNumber, bodyNumber, engineDisplacement,
                    fuelType, allowableWeight, technicalPassport, checkup, insurance,
                    hullInsurance, linearFCR, summerInCityFCR, summerOutCityFCR,
                    winterInCityFCR, winterOutCityFCR
                )
            )
            this.finish()
        } catch (hfe: HttpFormatException) {
            Toast.makeText(
                this,
                resources.getText(R.string.not_valid_url),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    internal inner class HttpFormatException : Exception()
}