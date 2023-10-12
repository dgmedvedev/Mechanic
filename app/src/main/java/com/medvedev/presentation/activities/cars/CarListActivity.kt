package com.medvedev.presentation.activities.cars

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ReportFragment.Companion.reportFragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityListCarBinding
import com.medvedev.presentation.CarViewModel
import com.medvedev.presentation.activities.MechanicActivity
import com.medvedev.presentation.adapters.CarListAdapter
import com.medvedev.presentation.pojo.Car

class CarListActivity : AppCompatActivity() {

    //private val prefsManagerCar = MechanicActivity.appPrefManager

    private lateinit var adapterCar: CarListAdapter

    private val binding by lazy {
        ActivityListCarBinding.inflate(layoutInflater)
    }

    private val carViewModel: CarViewModel by lazy {
        ViewModelProvider(this)[CarViewModel::class.java]
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        adapterCar = CarListAdapter()

        observeViewModel()

        //getListCars()
        //setListeners()
        //initRecyclerView()
        binding.carsRecyclerView.adapter = adapterCar
        binding.searchEditText.setTextColor(getColor(R.color.blue))
    }

//    override fun onStop() {
//        super.onStop()
//        //prefsManagerCar.saveSharedPrefsCars(SingletonCar.listToJson())
//    }

    override fun onResume() {
        super.onResume()
        //adapterCar.submitList(SingletonCar.getListCar())
    }

    private fun observeViewModel() {
        carViewModel.carList.observe(this) {
            adapterCar.submitList(it)
        }
    }

//    private fun getListCars() {
//        if (prefsManagerCar.getSharedPrefsCars() == "")
//            prefsManagerCar.saveSharedPrefsCars(SingletonCar.listToJson())
//
//        val listToJson = prefsManagerCar.getSharedPrefsCars()
//        val listFromJson = SingletonCar.listFromJson(listToJson)
//
//        if (listToJson != "[]")
//            SingletonCar.setListCars(listFromJson)
//    }

    private fun setListeners() {
        adapterCar.onCarClickListener = {
            launchCarDetailsActivity(it.id)
        }

        binding.addButton.setOnClickListener {
            launchCarEditActivity()
        }

        val listFromDb = adapterCar.currentList

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                runOnUiThread {
                    adapterCar.apply {
                        val list = filter(listFromDb, p0.toString())
                        adapterCar.submitList(list as MutableList<Car>)
                    }
                    //adapterCar.submitList(SingletonCar.filter(p0.toString()) as MutableList<Car>)
                }
            }
        })
    }

    private fun initRecyclerView() {
        binding.carsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CarListActivity)
            isNestedScrollingEnabled = false
        }
    }

    private fun launchCarEditActivity() {
        val intent = Intent(this, CarEditActivity::class.java)
        startActivity(intent)
    }

    private fun launchCarDetailsActivity(id: String) {
        val intent = CarDetailsActivity.getIntent(this, id)
        startActivity(intent)
    }
}