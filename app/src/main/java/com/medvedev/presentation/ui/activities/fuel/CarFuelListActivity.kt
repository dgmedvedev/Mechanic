package com.medvedev.presentation.ui.activities.fuel

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.medvedev.mechanic.databinding.ActivityListCarBinding
import com.medvedev.presentation.CarViewModel
import com.medvedev.presentation.adapters.CarListAdapter
import com.medvedev.presentation.pojo.Car

class CarFuelListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListCarBinding.inflate(layoutInflater)
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
            launchCarFuelDetailsActivity(it.id)
        }

        binding.addButton.setOnClickListener {
            launchCarFuelEditActivity()
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

    private fun launchCarFuelEditActivity() {
        val intent = Intent(this, CarFuelEditActivity::class.java)
        startActivity(intent)
    }

    private fun launchCarFuelDetailsActivity(idCar: String) {
        val intent = CarFuelDetailsActivity.getIntent(this, idCar)
        startActivity(intent)
    }
}