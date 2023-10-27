package com.medvedev.presentation.ui.fragments.cars

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.FragmentCarDetailsBinding
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.viewmodel.CarViewModel
import kotlinx.coroutines.launch

class CarDetailsFragment : Fragment() {

    private var _binding: FragmentCarDetailsBinding? = null
    private val binding: FragmentCarDetailsBinding
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
        _binding = FragmentCarDetailsBinding.inflate(inflater, container, false)
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
                setListeners(it, idCar)
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
                carViewModel.finishWork()
            }
        }

        binding.edit.setOnClickListener {
            launchFragment(CarEditFragment.getInstanceCarEdit(idCar))
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.car_container, fragment)
            .addToBackStack(null)
            .commit()
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getInstance(idCar: String): CarDetailsFragment {
            return CarDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_CAR, idCar)
                }
            }
        }
    }
}