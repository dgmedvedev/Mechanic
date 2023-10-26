package com.medvedev.presentation.ui.fragments.cars

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.FragmentCarEditBinding
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.viewmodel.CarViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class CarEditFragment : Fragment() {

    private var _binding: FragmentCarEditBinding? = null
    private val binding: FragmentCarEditBinding
        get() = _binding ?: throw RuntimeException(
            String.format(
                getString(R.string.binding_exception),
                binding.javaClass.simpleName
            )
        )

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var screenMode: String = MODE_UNKNOWN
    private var idCar: String? = null

    private val pattern = Patterns.WEB_URL

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw java.lang.RuntimeException("Activity must implement OnEditingFinishedListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCarEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        val args = requireArguments()

        if (!args.containsKey(EXTRA_SCREEN_MODE)) {
            throw java.lang.RuntimeException(getString(R.string.param_screen_mode_exception))
        }
        val mode = args.getString(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException(getString(R.string.screen_mode_exception) + mode)
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!args.containsKey(EXTRA_ID_CAR)) {
                throw java.lang.RuntimeException(getString(R.string.param_id_car_exception))
            }
            idCar = args.getString(EXTRA_ID_CAR)
        }
    }

    private fun observeViewModel() {
        carViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
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
            carViewModel.finishWork()
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
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val EXTRA_ID_CAR = "extra_id_car"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""

        fun getInstanceCarAdd(): CarEditFragment {
            return CarEditFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_ADD)
                }
            }
        }

        fun getInstanceCarEdit(idCar: String?): CarEditFragment {
            return CarEditFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_SCREEN_MODE, MODE_EDIT)
                    putString(EXTRA_ID_CAR, idCar)
                }
            }
        }
    }

    internal inner class HttpFormatException : Exception()
}