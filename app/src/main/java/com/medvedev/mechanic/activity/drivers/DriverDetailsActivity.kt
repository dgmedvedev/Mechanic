package com.medvedev.mechanic.activity.drivers

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.medvedev.mechanic.R
import kotlinx.android.synthetic.main.activity_details_driver.*

class DriverDetailsActivity : Activity() {

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
        setContentView(R.layout.activity_details_driver)

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

            nameTextView.text = user.name
            surnameTextView.text = user.surname
            middleNameTextView.text = user.middleName
            birthdayTextView.text = user.birthday
            drivingLicenseNumberTextView.text = user.drivingLicenseNumber
            drivingLicenseValidityTextView.text = user.drivingLicenseValidity
            medicalCertificateTextView.text = user.medicalCertificateValidity
        }

        delete.setOnClickListener {
            SingletonDriver.getListDriver().remove(user)
            this.finish()
        }

        edit.setOnClickListener {
            startActivity(DriverEditActivity.getIntent(this@DriverDetailsActivity, idDriver))
            this.finish()
        }
    }
}