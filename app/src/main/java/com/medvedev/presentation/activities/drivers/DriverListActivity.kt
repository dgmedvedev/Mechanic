package com.medvedev.presentation.activities.drivers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.medvedev.mechanic.databinding.ActivityListDriverBinding
import com.medvedev.presentation.activities.MechanicActivity
import com.medvedev.presentation.adapters.DriverListAdapter
import com.medvedev.presentation.pojo.Driver

class DriverListActivity : Activity() {

    private val prefsManagerDriver = MechanicActivity.appPrefManager

    private val adapterDriver by lazy {
        DriverListAdapter()
    }

    private val binding by lazy {
        ActivityListDriverBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getListDrivers()
        setListeners()
        initRecyclerView()
    }

    override fun onStop() {
        super.onStop()
        prefsManagerDriver.saveSharedPrefsDrivers(SingletonDriver.listToJson())
    }

    override fun onResume() {
        super.onResume()
        adapterDriver.submitList(SingletonDriver.getListDriver())
    }

    private fun getListDrivers() {
        if (prefsManagerDriver.getSharedPrefsDrivers() == "")
            prefsManagerDriver.saveSharedPrefsDrivers(SingletonDriver.listToJson())

        val listToJson = prefsManagerDriver.getSharedPrefsDrivers()
        val listFromJson = SingletonDriver.listFromJson(listToJson)

        if (listToJson != "[]")
            SingletonDriver.setListDrivers(listFromJson)
    }

    private fun setListeners() {
        adapterDriver.onDriverClickListener = object : DriverListAdapter.OnDriverClickListener {
            override fun onItemClick(item: Driver) {
                launchDriverDetailsActivity(item.id)
            }
        }

        binding.addButton.setOnClickListener {
            launchDriverEditActivity()
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                runOnUiThread {
                    adapterDriver.submitList(SingletonDriver.filter(p0.toString()) as MutableList<Driver>)
                }
            }
        })
    }

    private fun initRecyclerView() {
        binding.driversRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DriverListActivity)
            isNestedScrollingEnabled = false
            adapter = adapterDriver
        }
    }

    private fun launchDriverEditActivity() {
        val intent = Intent(this, DriverEditActivity::class.java)
        startActivity(intent)
    }

    private fun launchDriverDetailsActivity(idDriver: String) {
        val intent = DriverDetailsActivity.getIntent(this, idDriver)
        startActivity(intent)
    }
}