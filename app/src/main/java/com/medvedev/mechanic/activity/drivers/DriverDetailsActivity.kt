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

    companion object {
        private const val ID_DRIVER = "ID_DRIVER"

        fun getIntent(context: Context, idDriver: String): Intent {
            val intent = Intent(context, DriverDetailsActivity::class.java)
            intent.putExtra(ID_DRIVER, idDriver)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val idDriver = intent.getStringExtra(ID_DRIVER)
        val user: Driver? = SingletonDriver.getDriverById(idDriver)
        if (user == null) {
            Toast.makeText(
                this,
                resources.getText(R.string.id_not_found),
                Toast.LENGTH_SHORT
            ).show()
            this.finish()
        }

        user?.run {

            binding.nameTextView.text = user.name
            binding.surnameTextView.text = user.surname
            binding.middleNameTextView.text = user.middleName
            binding.birthdayTextView.text = user.birthday
            binding.drivingLicenseNumberTextView.text = user.drivingLicenseNumber
            binding.drivingLicenseValidityTextView.text = user.drivingLicenseValidity
            binding.medicalCertificateTextView.text = user.medicalCertificateValidity
        }

        binding.delete.setOnClickListener {
            SingletonDriver.getListDriver().remove(user)
            this.finish()
        }

        binding.edit.setOnClickListener {
            startActivity(DriverEditActivity.getIntent(this@DriverDetailsActivity, idDriver))
            this.finish()
        }
    }
}