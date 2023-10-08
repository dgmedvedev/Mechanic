package com.medvedev.mechanic.activity.drivers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityEditDriverBinding
import java.text.SimpleDateFormat
import java.util.*

class DriverEditActivity : Activity() {

    private var idDriver: String? = null

    private val pattern = Patterns.WEB_URL

    private val binding by lazy {
        ActivityEditDriverBinding.inflate(layoutInflater)
    }

    private val dateFormat = SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        idDriver = intent.getStringExtra(ID_DRIVER)

        val driver: Driver? = SingletonDriver.getDriverById(idDriver)

        driver?.let {
            initDriver(it)
        }

        setListeners(driver)
    }

    private fun initDriver(driver: Driver) {
        with(binding) {
            nameEditText.setText(driver.name)
            surnameEditText.setText(driver.surname)
            middleNameEditText.setText(driver.middleName)
            birthdayEditText.setText(driver.birthday)
            drivingLicenseNumberEditText.setText(driver.drivingLicenseNumber)
            drivingLicenseValidityEditText.setText(driver.drivingLicenseValidity)
            medicalCertificateEditText.setText(driver.medicalCertificateValidity)
        }
    }

    private fun addDriver(driver: Driver?) {
        var id = System.currentTimeMillis().toString()
        val name = binding.nameEditText.text.toString()
        val surname = binding.surnameEditText.text.toString()
        val middleName = binding.middleNameEditText.text.toString()

        val birthday = binding.birthdayEditText.text.toString()
        val drivingLicenseNumber = binding.drivingLicenseNumberEditText.text.toString()
        val drivingLicenseValidity = binding.drivingLicenseValidityEditText.text.toString()
        val medicalCertificateValidity = binding.medicalCertificateEditText.text.toString()

        // imageUrl в разработке
        //var imageUrl = user?.imageUrl ?: ""
        var imageUrl = getString(R.string.image_url)
        if (BuildConfig.DEBUG) {
            imageUrl = getString(R.string.image_url)
        }

        try {
            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            if (idDriver != null) {
                driver?.let {
                    id = it.id
                    SingletonDriver.deleteDriver(it)
                }
            }

            SingletonDriver.addDriver(
                Driver(
                    id,
                    if (name == "") resources.getString(R.string.name) else name,
                    if (surname == "") resources.getString(R.string.surname) else surname,
                    middleName,
                    imageUrl,
                    birthday,
                    drivingLicenseNumber,
                    drivingLicenseValidity,
                    medicalCertificateValidity
                )
            )
            finish()
        } catch (hfe: HttpFormatException) {
            showToast(resources.getText(R.string.not_valid_url))
        }
    }

    private fun setListeners(driver: Driver?) {
        binding.save.setOnClickListener {
            addDriver(driver)
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val ID_DRIVER = "ID_DRIVER"

        fun getIntent(
            context: Context,
            idDriver: String? = System.currentTimeMillis().toString()
        ): Intent {
            val intent = Intent(context, DriverEditActivity::class.java)
            intent.putExtra(ID_DRIVER, idDriver)
            return intent
        }
    }

    internal inner class HttpFormatException : Exception()
}