package com.medvedev.mechanic.activity.fuel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.medvedev.mechanic.activity.cars.Car
import com.medvedev.mechanic.activity.cars.SingletonCar
import com.medvedev.mechanic.adapters.CarListAdapter
import com.medvedev.mechanic.databinding.ActivityListCarBinding
import com.medvedev.utils.AppPrefManagerCar

class CarFuelListActivity : Activity() {

    private lateinit var prefsManagerCar: AppPrefManagerCar

    private val adapterCar by lazy {
        CarListAdapter(SingletonCar.getListCar())
    }

    private val binding by lazy {
        ActivityListCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getListCars()
        setViewListeners()
        initRecyclerView()
    }

    override fun onStop() {
        super.onStop()
        prefsManagerCar.saveUserText(SingletonCar.listToJson(SingletonCar.getListCar()))
    }

    override fun onResume() {
        super.onResume()
        adapterCar.updateList(SingletonCar.getListCar())
    }

    private fun getListCars() {
        prefsManagerCar = AppPrefManagerCar(this)

        if (prefsManagerCar.getUserText() == "")
            prefsManagerCar.saveUserText(SingletonCar.listToJson(SingletonCar.getListCar()))

        val listToJson = prefsManagerCar.getUserText()
        val listFromJson = SingletonCar.listFromJson(listToJson)

        if (listToJson != "[]")
            SingletonCar.setListCars(listFromJson)
    }

    private fun setViewListeners() {
        adapterCar.onCarClickListener = object : CarListAdapter.OnCarClickListener {
            override fun onItemClick(item: Car) {
                val intent = CarFuelDetailsActivity.getIntent(this@CarFuelListActivity, item.id)
                startActivity(intent)
            }
        }

        binding.addButton.setOnClickListener {
            launchCarFuelEditActivity()
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            var timer: Handler? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                timer = Handler()
                timer?.postDelayed({
                    adapterCar.updateList(SingletonCar.filter(p0.toString()) as MutableList<Car>)
                }, 100)
            }
        })
    }

    private fun initRecyclerView() {
        binding.carsRecyclerView.setHasFixedSize(true)
        binding.carsRecyclerView.layoutManager = LinearLayoutManager(this)
        binding.carsRecyclerView.isNestedScrollingEnabled = false
        binding.carsRecyclerView.adapter = adapterCar
    }

    private fun launchCarFuelEditActivity() {
        val intent = Intent(this, CarFuelEditActivity::class.java)
        startActivity(intent)
    }
}