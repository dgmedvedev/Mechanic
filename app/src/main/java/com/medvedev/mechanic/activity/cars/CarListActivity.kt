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
import com.medvedev.utils.AppPrefManagerCar
import kotlinx.android.synthetic.main.activity_list_car.*

class CarListActivity : Activity(), CarListAdapter.ClickListener {
    private lateinit var recyclerView: RecyclerView
    private var adapterCar: CarListAdapter? = null

    private lateinit var prefsManagerCar: AppPrefManagerCar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_car)

        prefsManagerCar = AppPrefManagerCar(this)

        if (prefsManagerCar.getUserText() == "")
            prefsManagerCar.saveUserText(SingletonCar.listToJson())

        val listToJson = prefsManagerCar.getUserText()

        val listFromJson = SingletonCar.listFromJson(listToJson)

        if (listToJson != "[]")
            SingletonCar.setListCars(listFromJson)

        recyclerView = findViewById(R.id.carsRecyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
        adapterCar = CarListAdapter(SingletonCar.getListCar(), this)

        recyclerView.adapter = adapterCar

        searchEditText.addTextChangedListener(object : TextWatcher {

            var timer: Handler? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                timer = Handler()
                timer?.postDelayed({
                    adapterCar?.updateList(SingletonCar.filter(p0.toString()) as MutableList<Car>)
                }, 500)
            }
        })

        addButton.setOnClickListener {
            startCarEditActivity()
        }
    }

    override fun onStop() {
        super.onStop()
        prefsManagerCar.saveUserText(SingletonCar.listToJson())
    }

    override fun onResume() {
        super.onResume()
        adapterCar?.updateList(SingletonCar.getListCar())
    }

    private fun startCarEditActivity() {
        val intent = Intent(this, CarEditActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClick(item: Car) {
        val intent = CarDetailsActivity.getIntent(this@CarListActivity, item.id)
        startActivity(intent)
    }
}