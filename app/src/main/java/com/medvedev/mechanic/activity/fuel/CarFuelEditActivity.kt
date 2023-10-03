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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idCar = intent.getStringExtra(ID_CAR)

        val user: Car? = SingletonCar.getCarById(idCar)

        user?.run {
            binding.brandEditText.setText(user.brand)
            binding.modelEditText.setText(user.model)
            binding.yearProductionEditText.setText(user.yearProduction.toString())
            binding.stateNumberEditText.setText(user.stateNumber)

            binding.linearFCREditText.setText(user.linearFuelConsumptionRate)
            binding.summerInCityEditText.setText(user.summerInCityFuelConsumptionRate)
            binding.summerOutCityEditText.setText(user.summerOutCityFuelConsumptionRate)
            binding.winterInCityEditText.setText(user.winterInCityFuelConsumptionRate)
            binding.winterOutCityEditText.setText(user.winterOutCityFuelConsumptionRate)
        }

        binding.save.setOnClickListener {
            val id = System.currentTimeMillis().toString()
            val brand = binding.brandEditText.text.toString()
            val model = binding.modelEditText.text.toString()
            val stateNumber = binding.stateNumberEditText.text.toString()

            val engineDisplacement = user?.engineDisplacement ?: ""
            val fuelType = user?.fuelType ?: ""
            val bodyNumber = user?.bodyNumber ?: ""
            val allowableWeight = user?.allowableWeight ?: ""
            val technicalPassport = user?.technicalPassport ?: ""
            val checkup = user?.checkup ?: ""
            val insurance = user?.insurance ?: ""
            val hullInsurance = user?.hullInsurance ?: ""

            val linearFCR = binding.linearFCREditText.text.toString()
            val summerInCityFCR = binding.summerInCityEditText.text.toString()
            val summerOutCityFCR = binding.summerOutCityEditText.text.toString()
            val winterInCityFCR = binding.winterInCityEditText.text.toString()
            val winterOutCityFCR = binding.winterInCityEditText.text.toString()

            // imageUrl в разработке
            //var imageUrl = user?.imageUrl ?: ""
            var imageUrl = "https://clck.ru/Gx4Nd"
            if (BuildConfig.DEBUG) {
                imageUrl = "https://clck.ru/Gx4Nd"
            }

            try {
                val yearProduction = binding.yearProductionEditText.text.toString().toInt()

                if (idCar != null) {
                    SingletonCar.getListCar().remove(user)
                    user?.let {
                        addCar(
                            user.id,
                            brand,
                            model,
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
                    }
                } else {
                    addCar(
                        id,
                        brand,
                        model,
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
        stateNumber: String, engineDisplacement: String, fuelType: String,
        bodyNumber: String, allowableWeight: String, technicalPassport: String,
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
                    imageUrl, yearProduction, stateNumber, engineDisplacement, fuelType,
                    bodyNumber, allowableWeight, technicalPassport, checkup, insurance,
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