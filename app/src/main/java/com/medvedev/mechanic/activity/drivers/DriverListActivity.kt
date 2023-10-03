package com.medvedev.mechanic.activity.drivers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.medvedev.mechanic.R
import com.medvedev.mechanic.adapters.DriverListAdapter
import com.medvedev.mechanic.databinding.ActivityListDriverBinding
import com.medvedev.utils.AppPrefManagerDriver

class DriverListActivity : Activity(), DriverListAdapter.ClickListener {
    private lateinit var recyclerView: RecyclerView
    private var adapterDriver: DriverListAdapter? = null

    private lateinit var prefsManagerDriver: AppPrefManagerDriver

    private val binding by lazy {
        ActivityListDriverBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        prefsManagerDriver = AppPrefManagerDriver(this)

        if (prefsManagerDriver.getUserText() == "")
            prefsManagerDriver.saveUserText(SingletonDriver.listToJson())

        val listToJson = prefsManagerDriver.getUserText()

        val listFromJson = SingletonDriver.listFromJson(listToJson)

        if (listToJson != "[]")
            SingletonDriver.setListDrivers(listFromJson)

        recyclerView = findViewById(R.id.driversRecyclerView)

        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.isNestedScrollingEnabled = false
        adapterDriver = DriverListAdapter(SingletonDriver.getListDriver(), this)

        recyclerView.adapter = adapterDriver

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            var timer: Handler? = null
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                timer = Handler()
                timer?.postDelayed({
                    adapterDriver?.updateList(SingletonDriver.filter(p0.toString()) as MutableList<Driver>)
                }, 500)
            }
        })

        binding.addButton.setOnClickListener {
            startDriverEditActivity()
        }
    }

    override fun onStop() {
        super.onStop()
        prefsManagerDriver.saveUserText(SingletonDriver.listToJson())
    }

    override fun onResume() {
        super.onResume()
        adapterDriver?.updateList(SingletonDriver.getListDriver())
    }

    private fun startDriverEditActivity() {
        val intent = Intent(this, DriverEditActivity::class.java)
        startActivity(intent)
    }

    override fun onItemClick(item: Driver) {
        val intent = DriverDetailsActivity.getIntent(this@DriverListActivity, item.id)
        startActivity(intent)
    }
}