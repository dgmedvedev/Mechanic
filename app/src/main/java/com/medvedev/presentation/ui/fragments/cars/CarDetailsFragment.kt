package com.medvedev.presentation.ui.fragments.cars

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.medvedev.mechanic.R

class CarDetailsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_car_details, container, false)
    }

    private fun parseParams(){

    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(idCar: String): CarDetailsFragment {
            return CarDetailsFragment().apply {
                arguments = Bundle().apply {
                    putString(ID_CAR, idCar)
                }
            }
        }
    }
}