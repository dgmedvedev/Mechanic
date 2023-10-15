package com.medvedev.presentation.activities.drivers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityDetailsDriverBinding
import com.medvedev.presentation.DriverViewModel
import com.medvedev.presentation.pojo.Driver
import kotlinx.coroutines.launch

class DriverDetailsActivity : AppCompatActivity() {

    private val driverViewModel by lazy {
        ViewModelProvider(this)[DriverViewModel::class.java]
    }

    private val binding by lazy {
        ActivityDetailsDriverBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        lifecycleScope.launch {
            var driver: Driver? = null
            val idDriver = intent.getStringExtra(ID_DRIVER)

            idDriver?.let {
                driver = driverViewModel.getDriverById(it)
            }

            if (driver == null) {
                showToast(resources.getText(R.string.id_not_found))
                finish()
            }

            driver?.let { it ->
                initDriver(it)
                setListeners(it, idDriver)
            }
        }
    }

    private fun initDriver(driver: Driver) {
        with(binding) {
            nameTextView.text = driver.name
            surnameTextView.text = driver.surname
            middleNameTextView.text = driver.middleName
            birthdayTextView.text = driver.birthday
            drivingLicenseNumberTextView.text = driver.drivingLicenseNumber
            drivingLicenseValidityTextView.text = driver.drivingLicenseValidity
            medicalCertificateTextView.text = driver.medicalCertificateValidity
        }
    }

    private fun setListeners(driver: Driver, idDriver: String?) {
        binding.delete.setOnClickListener {
            lifecycleScope.launch {
                driverViewModel.deleteDriver(driver)
                finish()
            }
        }

        binding.edit.setOnClickListener {
            launchDriverEditActivity(idDriver)
            finish()
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchDriverEditActivity(idDriver: String?) {
        val intent = DriverEditActivity.getIntent(this, idDriver)
        startActivity(intent)
    }

    companion object {
        private const val ID_DRIVER = "ID_DRIVER"

        fun getIntent(context: Context, idDriver: String): Intent {
            val intent = Intent(context, DriverDetailsActivity::class.java)
            intent.putExtra(ID_DRIVER, idDriver)
            return intent
        }
    }
}