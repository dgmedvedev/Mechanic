package com.medvedev.mechanic.activity.cars

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.adapters.CarListAdapter
import com.medvedev.mechanic.databinding.ActivityListCarBinding
import com.medvedev.utils.AppPrefManagerCar

class CarListActivity : Activity() {

    private lateinit var prefsManagerCar: AppPrefManagerCar

    private lateinit var recyclerView: RecyclerView

    private val adapterCar by lazy {
        CarListAdapter(SingletonCar.getListCar())
    }

    private val binding by lazy {
        ActivityListCarBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefsManagerCar = AppPrefManagerCar(this)

        if (prefsManagerCar.getUserText() == "")
            prefsManagerCar.saveUserText(SingletonCar.listToJson(SingletonCar.getListCar()))

        val listToJson = prefsManagerCar.getUserText()

        val listFromJson = SingletonCar.listFromJson(listToJson)

        if (listToJson != "[]")
            SingletonCar.setListCars(listFromJson)

        recyclerView = findViewById(R.id.carsRecyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false

        recyclerView.adapter = adapterCar

        setAdapterClickListener()

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            var timer: Handler? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                timer = Handler()
                timer?.postDelayed({
                    adapterCar.updateList(SingletonCar.filter(p0.toString()) as MutableList<Car>)
                }, 500)
            }
        })

        binding.addButton.setOnClickListener {
            startCarEditActivity()
        }
    }

    override fun onStop() {
        super.onStop()
        prefsManagerCar.saveUserText(SingletonCar.listToJson(SingletonCar.getListCar()))
    }

    override fun onResume() {
        super.onResume()
        adapterCar.updateList(SingletonCar.getListCar())
    }

    private fun setAdapterClickListener() {
        adapterCar.onCarClickListener = object : CarListAdapter.OnCarClickListener {
            override fun onItemClick(item: Car) {
                val intent = CarDetailsActivity.getIntent(this@CarListActivity, item.id)
                startActivity(intent)
            }
        }
    }

    private fun startCarEditActivity() {
        val intent = Intent(this, CarEditActivity::class.java)
        startActivity(intent)
    }
}