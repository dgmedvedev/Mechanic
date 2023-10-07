package com.medvedev.mechanic.activity.fuel

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
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
        //CarListAdapter(SingletonCar.getListCar())
        CarListAdapter()
    }

    private val timer by lazy {
        Handler(Looper.getMainLooper())
    }

    private val binding by lazy {
        ActivityListCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getListCars()
        setListeners()
        initRecyclerView()
    }

    override fun onStop() {
        super.onStop()
        prefsManagerCar.saveUserText(SingletonCar.listToJson(SingletonCar.getListCar()))
    }

    override fun onResume() {
        super.onResume()
        //adapterCar.updateList(SingletonCar.getListCar())
    }

    private fun getListCars() {
        prefsManagerCar = AppPrefManagerCar(this)

        if (prefsManagerCar.getUserText() == "")
            prefsManagerCar.saveUserText(SingletonCar.listToJson(SingletonCar.getListCar()))

        val listToJson = prefsManagerCar.getUserText()
        val listFromJson = SingletonCar.listFromJson(listToJson)

        if (listToJson != "[]")
            SingletonCar.setListCars(listFromJson)

        adapterCar.submitList(SingletonCar.getListCar())
    }

    private fun setListeners() {
//        adapterCar.onCarClickListener = object : CarListAdapter.OnCarClickListener {
//            override fun onItemClick(item: Car) {
//                launchCarFuelDetailsActivity(item.id)
//            }
//        }

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
//                timer.postDelayed({
//                    adapterCar.updateList(SingletonCar.filter(p0.toString()) as MutableList<Car>)
//                }, 100)
            }
        })
    }

    private fun initRecyclerView() {
        binding.carsRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CarFuelListActivity)
            isNestedScrollingEnabled = false
            adapter = adapterCar
        }
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