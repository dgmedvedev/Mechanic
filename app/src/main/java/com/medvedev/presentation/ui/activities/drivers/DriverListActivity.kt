package com.medvedev.presentation.ui.activities.drivers

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.medvedev.domain.pojo.Driver
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityDriverListBinding
import com.medvedev.presentation.adapter.driver.DriverListAdapter
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.drivers.DriverDetailsFragment
import com.medvedev.presentation.ui.fragments.drivers.DriverEditFragment
import com.medvedev.presentation.viewmodel.DriverViewModel

class DriverListActivity : AppCompatActivity(), OnEditingFinishedListener {

    private val binding by lazy {
        ActivityDriverListBinding.inflate(layoutInflater)
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

    override fun onEditingFinished() {
        supportFragmentManager.popBackStack()
    }

    private fun observeViewModel() {
        driverViewModel.driverListLD.observe(this) {
            adapterDriver.submitList(it)
            listFromDb = it
        }
    }

    private fun setListeners() {
        adapterDriver.onDriverClickListener = {
            if (isLandOrientation()) {
                launchFragment(DriverDetailsFragment.getInstance(it.id))
            } else {
                launchDriverDetailsActivity(it.id)
            }
        }

        binding.addButton.setOnClickListener {
            if (isLandOrientation()) {
                launchFragment(DriverEditFragment.getInstanceDriverAdd())
            } else {
                launchDriverEditActivity()
            }
        }

        binding.searchEditText.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun afterTextChanged(p0: Editable?) {
                val desiredList = driverViewModel.filter(listFromDb, p0.toString())
                adapterDriver.submitList(desiredList as MutableList<Driver>)
            }
        })
    }

    private fun isLandOrientation(): Boolean {
        return binding.driverContainer != null
    }

    private fun launchDriverEditActivity() {
        val intent = DriverEditActivity.getIntentAddDriver(this)
        startActivity(intent)
    }

    private fun launchDriverDetailsActivity(idDriver: String) {
        val intent = DriverDetailsActivity.getIntent(this, idDriver)
        startActivity(intent)
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.popBackStack()
        supportFragmentManager.beginTransaction()
            .replace(R.id.driver_container, fragment)
            .addToBackStack(null)
            .commit()
    }
}