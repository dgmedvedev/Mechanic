package com.medvedev.presentation.ui.fragments.fuel

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
import androidx.viewbinding.BuildConfig
import com.medvedev.domain.pojo.Car
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.FragmentCarEditBinding
import com.medvedev.mechanic.databinding.FragmentCarFuelEditBinding
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.cars.CarEditFragment
import com.medvedev.presentation.viewmodel.CarViewModel
import kotlinx.coroutines.launch

class CarFuelEditFragment : Fragment() {

    private var _binding: FragmentCarFuelEditBinding? = null
    private val binding: FragmentCarFuelEditBinding
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

    private var idCar: String? = null

    private val pattern = Patterns.WEB_URL

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnEditingFinishedListener) {
            onEditingFinishedListener = context
        } else {
            throw java.lang.RuntimeException(getString(R.string.implement_exception))
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
        _binding = FragmentCarFuelEditBinding.inflate(inflater, container, false)
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
        idCar = arguments?.getString(EXTRA_ID_CAR)
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

            etLinearFCR.setText(car.linearFuelConsumptionRate)
            etSummerInCity.setText(car.summerInCityFuelConsumptionRate)
            etSummerOutCity.setText(car.summerOutCityFuelConsumptionRate)
            etWinterInCity.setText(car.winterInCityFuelConsumptionRate)
            etWinterOutCity.setText(car.winterOutCityFuelConsumptionRate)
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

        val linearFCR = binding.etLinearFCR.text.toString()
        val summerInCityFCR = binding.etSummerInCity.text.toString()
        val summerOutCityFCR = binding.etSummerOutCity.text.toString()
        val winterInCityFCR = binding.etWinterInCity.text.toString()
        val winterOutCityFCR = binding.etWinterOutCity.text.toString()

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
        private const val EXTRA_ID_CAR = "ID_CAR"

        fun getInstanceCarAdd(): CarFuelEditFragment {
            return CarFuelEditFragment()
        }

        fun getInstanceCarEdit(idCar: String?): CarFuelEditFragment {
            return CarFuelEditFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ID_CAR, idCar)
                }
            }
        }
    }

    internal inner class HttpFormatException : Exception()
}