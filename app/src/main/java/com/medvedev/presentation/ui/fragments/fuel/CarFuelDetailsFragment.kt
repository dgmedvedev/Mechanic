package com.medvedev.presentation.ui.fragments.fuel

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.FragmentCarFuelDetailsBinding
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.activities.fuel.CarFuelDetailsActivity
import com.medvedev.presentation.ui.activities.fuel.CarFuelEditActivity
import com.medvedev.presentation.ui.fragments.cars.CarDetailsFragment
import com.medvedev.presentation.viewmodel.CarViewModel
import kotlinx.coroutines.launch

class CarFuelDetailsFragment : Fragment() {

    private var _binding: FragmentCarFuelDetailsBinding? = null
    private val binding: FragmentCarFuelDetailsBinding
        get() = _binding ?: throw RuntimeException("FragmentCarFuelDetailsBinding = null")

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private lateinit var onEditingFinishedListener: OnEditingFinishedListener

    private var idCar: String? = null

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
        _binding = FragmentCarFuelDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()

        lifecycleScope.launch {
            var car: Car? = null

            idCar?.let {
                car = carViewModel.getCarById(it)
            }

            if (car == null) {
                showToast(resources.getText(R.string.id_not_found))
                carViewModel.finishWork()
            }

            car?.let {
                initCar(it)
                setListeners(idCar)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun parseParams() {
        idCar = requireArguments().getString(ID_CAR)
    }

    private fun observeViewModel() {
        carViewModel.shouldCloseScreen.observe(viewLifecycleOwner) {
            onEditingFinishedListener.onEditingFinished()
        }
    }

    private fun initCar(car: Car) {
        with(binding) {
            brandTextView.text = car.brand
            modelTextView.text = car.model
            yearProductionTextView.text = car.yearProduction.toString()
            stateNumberTextView.text = car.stateNumber
            linearFCRTextView.text = car.linearFuelConsumptionRate
            summerInCityTextView.text = car.summerInCityFuelConsumptionRate
            summerOutCityTextView.text = car.summerOutCityFuelConsumptionRate
            winterInCityTextView.text = car.winterInCityFuelConsumptionRate
            winterOutCityTextView.text = car.winterOutCityFuelConsumptionRate
        }
    }

    private fun setListeners(idCar: String?) {
        binding.edit.setOnClickListener {
            //launchFragment(CarFuelEditFragment.getInstance())
            carViewModel.finishWork()
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.car_details_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getInstance(idCar: String): CarFuelDetailsFragment {
            return CarFuelDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_CAR, idCar)
                }
            }
        }
    }
}