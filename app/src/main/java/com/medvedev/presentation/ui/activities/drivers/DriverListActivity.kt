package com.medvedev.presentation.ui.activities.drivers

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.medvedev.mechanic.databinding.ActivityListDriverBinding
import com.medvedev.presentation.DriverViewModel
import com.medvedev.presentation.adapters.DriverListAdapter
import com.medvedev.presentation.pojo.Driver

class DriverListActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityListDriverBinding.inflate(layoutInflater)
    }

    private val driverViewModel by lazy {
        ViewModelProvider(this)[DriverViewModel::class.java]
    }

    private val adapterDriver by lazy {
        DriverListAdapter()
    }

    private lateinit var listFromDb: List<Driver>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        observeViewModel()
        setListeners()

        binding.driversRecyclerView.adapter = adapterDriver
    }

    private fun observeViewModel() {
        driverViewModel.driverListLD.observe(this) {
            adapterDriver.submitList(it)
            listFromDb = it
        }
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
                val desiredList = driverViewModel.filter(listFromDb, p0.toString())
                adapterDriver.submitList(desiredList)
            }
        })
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