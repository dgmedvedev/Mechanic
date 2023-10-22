package com.medvedev.presentation.ui.activities.cars

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityCarListBinding
import com.medvedev.presentation.viewmodel.CarViewModel
import com.medvedev.presentation.adapter.car.CarListAdapter
import com.medvedev.presentation.pojo.Car
import com.medvedev.presentation.ui.fragments.cars.CarDetailsFragment
import com.medvedev.presentation.ui.fragments.cars.CarEditFragment

class CarListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityCarListBinding.inflate(layoutInflater)
    }

    private val carViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }

    private val adapterCar by lazy {
        CarListAdapter()
    }

    private lateinit var listFromDb: List<Car>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel()
        setListeners()

        binding.carsRecyclerView.adapter = adapterCar
    }

    private fun observeViewModel() {
        carViewModel.carListLD.observe(this) {
            adapterCar.submitList(it)
            listFromDb = it
        }
    }

    private fun setListeners() {
        adapterCar.onCarClickListener = {
            if (isLandOrientation()) {
                showToast("id = ${it.id}")

                launchFragment(CarDetailsFragment.getInstanceCarDetails(it.id))
            } else {
                showToast("id = ${it.id}")
                launchCarDetailsActivity(it.id)
            }
        }

        binding.addButton.setOnClickListener {
            if (isLandOrientation()) {
                launchFragment(CarEditFragment.getInstanceCarAdd())
            } else {
                launchCarEditActivity()
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val desiredList = carViewModel.filter(listFromDb, p0.toString())
                adapterCar.submitList(desiredList)
            }
        })
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun isLandOrientation(): Boolean {
        return binding.carContainer != null
    }

    private fun launchCarEditActivity() {
        val intent = Intent(this, CarEditActivity::class.java)
        startActivity(intent)
    }

    private fun launchCarDetailsActivity(id: String) {
        val intent = CarDetailsActivity.getIntent(this, id)
        startActivity(intent)
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.carContainer, fragment)
            .addToBackStack(null)
            .commit()
    }
}