package com.medvedev.presentation.ui.activities.fuel

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.medvedev.domain.pojo.Car
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityCarListBinding
import com.medvedev.presentation.adapter.car.CarListAdapter
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.fuel.CarFuelDetailsFragment
import com.medvedev.presentation.ui.fragments.fuel.CarFuelEditFragment
import com.medvedev.presentation.viewmodel.CarViewModel

class CarFuelListActivity : AppCompatActivity(), OnEditingFinishedListener {

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

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
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
                launchFragment(CarFuelDetailsFragment.getInstance(it.id))
            } else {
                launchCarFuelDetailsActivity(it.id)
            }
        }

        binding.addButton.setOnClickListener {
            if (isLandOrientation()) {
                launchFragment(CarFuelEditFragment.getInstanceCarAdd())
            } else {
                launchCarFuelEditActivity()
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val desiredList = carViewModel.filter(listFromDb, p0.toString())
                adapterCar.submitList(desiredList as MutableList<Car>)
            }
        })
    }

    private fun isLandOrientation(): Boolean {
        return binding.carContainer != null
    }

    private fun launchCarFuelEditActivity() {
        val intent = CarFuelEditActivity.getIntentAddCar(this)
        startActivity(intent)
    }

    private fun launchCarFuelDetailsActivity(idCar: String) {
        val intent = CarFuelDetailsActivity.getIntent(this, idCar)
        startActivity(intent)
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.car_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}