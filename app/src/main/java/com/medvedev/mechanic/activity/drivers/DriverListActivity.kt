package com.medvedev.mechanic.activity.drivers

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.recyclerview.widget.LinearLayoutManager
import com.medvedev.mechanic.adapters.DriverListAdapter
import com.medvedev.mechanic.databinding.ActivityListDriverBinding
import com.medvedev.utils.AppPrefManagerDriver

class DriverListActivity : Activity() {

    private lateinit var prefsManagerDriver: AppPrefManagerDriver

    private val adapterDriver by lazy {
        DriverListAdapter(SingletonDriver.getListDriver())
    }

    private val timer by lazy {
        Handler(Looper.getMainLooper())
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
        prefsManagerDriver.saveUserText(SingletonDriver.listToJson())
    }

    override fun onResume() {
        super.onResume()
        adapterDriver.updateList(SingletonDriver.getListDriver())
    }

    private fun getListDrivers() {
        prefsManagerDriver = AppPrefManagerDriver(this)

        if (prefsManagerDriver.getUserText() == "")
            prefsManagerDriver.saveUserText(SingletonDriver.listToJson())

        val listToJson = prefsManagerDriver.getUserText()
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
                timer.postDelayed({
                    adapterDriver.updateList(SingletonDriver.filter(p0.toString()) as MutableList<Driver>)
                }, 100)
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