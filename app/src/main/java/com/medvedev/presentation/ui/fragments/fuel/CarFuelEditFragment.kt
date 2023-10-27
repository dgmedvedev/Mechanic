package com.medvedev.presentation.ui.fragments.fuel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.viewbinding.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityCarFuelEditBinding
import com.medvedev.mechanic.databinding.FragmentCarEditBinding
import com.medvedev.mechanic.databinding.FragmentCarFuelEditBinding
import com.medvedev.presentation.viewmodel.CarViewModel
import com.medvedev.presentation.pojo.Car
import kotlinx.coroutines.launch

class CarFuelEditFragment : AppCompatActivity() {

    private var _binding: FragmentCarFuelEditBinding? = null
    private val binding: FragmentCarFuelEditBinding
        get() = _binding ?: throw RuntimeException(
            String.format(
                getString(R.string.binding_exception),
                binding.javaClass.simpleName
            )
        )


    private var idCar: String? = null

    private val pattern = Patterns.WEB_URL

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

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

            linearFCREditText.setText(car.linearFuelConsumptionRate)
            summerInCityEditText.setText(car.summerInCityFuelConsumptionRate)
            summerOutCityEditText.setText(car.summerOutCityFuelConsumptionRate)
            winterInCityEditText.setText(car.winterInCityFuelConsumptionRate)
            winterOutCityEditText.setText(car.winterOutCityFuelConsumptionRate)
        }
    }

    private suspend fun addCar(car: Car?) {
        var id = System.currentTimeMillis().toString()
        val brand = binding.etBrand.text.toString()
        val model = binding.etModel.text.toString()
        val stateNumber = binding.etStateNumber.text.toString()

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
            val yearProduction = binding.etYearProduction.text.toString().toInt()

            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            if (idCar != null) {
                car?.let {
                    carViewModel.deleteCar(it)
                    id = it.id
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

        fun getInstance(
            context: Context,
            idCar: String? = System.currentTimeMillis().toString()
        ): Intent {
            val intent = Intent(context, CarFuelEditFragment::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }

    internal inner class HttpFormatException : Exception()
}