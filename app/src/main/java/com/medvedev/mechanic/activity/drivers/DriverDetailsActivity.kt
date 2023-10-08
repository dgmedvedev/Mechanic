package com.medvedev.mechanic.activity.drivers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityDetailsDriverBinding

class DriverDetailsActivity : Activity() {

    private val binding by lazy {
        ActivityDetailsDriverBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idDriver = intent.getStringExtra(ID_DRIVER)
        val driver: Driver? = SingletonDriver.getDriverById(idDriver).also {
            if (it == null) {
                showToast(resources.getText(R.string.id_not_found))
                finish()
            }
        }

        driver?.let {
            initDriver(it)
            setListeners(it, idDriver)
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
            SingletonDriver.deleteDriver(driver)
            finish()
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