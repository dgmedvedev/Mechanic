package com.medvedev.presentation.ui.fragments.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.R
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.viewmodel.CarViewModel
import kotlinx.coroutines.launch

class CarEditFragment : Fragment() {

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private var idCar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {

            var car: Car? = null
            idCar = requireArguments().getString(ID_CAR)

            idCar?.let {
                car = carViewModel.getCarById(it)
            }

            showToast("CarEditFragment, car = $car")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_edit, container, false)
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getInstanceCarAdd(): CarEditFragment {
            return CarEditFragment()
        }

        fun getInstanceCarEdit(idCar: String?): CarEditFragment {
            return CarEditFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_CAR, idCar)
                }
            }
        }
    }
}