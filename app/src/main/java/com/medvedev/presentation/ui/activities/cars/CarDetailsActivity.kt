package com.medvedev.presentation.ui.activities.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.medvedev.mechanic.databinding.ActivityCarDetailsBinding
import com.medvedev.presentation.ui.fragments.cars.CarDetailsFragment
import com.medvedev.presentation.viewmodel.CarViewModel

class CarDetailsActivity : AppCompatActivity(), CarDetailsFragment.OnEditingFinishedListener {

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private val binding by lazy {
        ActivityCarDetailsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


    }

    override fun onEditingFinished() {
        finish()
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(context: Context, idCar: String): Intent {
            val intent = Intent(context, CarDetailsActivity::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }
}