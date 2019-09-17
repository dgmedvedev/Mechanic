package com.medvedev.mechanic.activity.drivers

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.Toast
import com.medvedev.mechanic.BuildConfig
import com.medvedev.mechanic.R
import kotlinx.android.synthetic.main.activity_edit_driver.*
import java.text.SimpleDateFormat

class DriverEditActivity : Activity() {

    private var idDriver: String? = null

    private val pattern = Patterns.WEB_URL

    @SuppressLint("SimpleDateFormat")
    private val DATE_FORMAT = SimpleDateFormat("dd.MM.yyyy")

    companion object {
        private const val ID_DRIVER = "ID_DRIVER"

        fun getIntent(context: Context, idDriver: String? = System.currentTimeMillis().toString()): Intent {
            val intent = Intent(context, DriverEditActivity::class.java)
            intent.putExtra(ID_DRIVER, idDriver)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_driver)

        idDriver = intent.getStringExtra(ID_DRIVER)

        val user: Driver? = SingletonDriver.getDriverById(idDriver)

        user?.run {
            nameEditText.setText(user.name)
            surnameEditText.setText(user.surname)
            middleNameEditText.setText(user.middleName)
            birthdayEditText.setText(user.birthday)
            drivingLicenseNumberEditText.setText(user.drivingLicenseNumber)
            drivingLicenseValidityEditText.setText(user.drivingLicenseValidity)
            medicalCertificateEditText.setText(user.medicalCertificateValidity)
        }

        save.setOnClickListener {
            val id = System.currentTimeMillis().toString()
            val name = nameEditText.text.toString()
            val surname = surnameEditText.text.toString()
            val middleName = middleNameEditText.text.toString()

            val birthday = birthdayEditText.text.toString()
            val drivingLicenseNumber = drivingLicenseNumberEditText.text.toString()
            val drivingLicenseValidity = drivingLicenseValidityEditText.text.toString()
            val medicalCertificateValidity = medicalCertificateEditText.text.toString()

            // imageUrl в разработке
            //var imageUrl = user?.imageUrl ?: ""
            var imageUrl = "https://clck.ru/Gx4Nd"
            if (BuildConfig.DEBUG) {
                imageUrl = "https://clck.ru/Gx4Nd"
            }

            if (idDriver != null) {
                SingletonDriver.getListDriver().remove(user)
                user?.let {
                    addDriver(
                        user.id,
                        name,
                        surname,
                        middleName,
                        imageUrl,
                        birthday,
                        drivingLicenseNumber,
                        drivingLicenseValidity,
                        medicalCertificateValidity
                    )
                }
            } else {
                addDriver(
                    id,
                    name,
                    surname,
                    middleName,
                    imageUrl,
                    birthday,
                    drivingLicenseNumber,
                    drivingLicenseValidity,
                    medicalCertificateValidity
                )
            }
        }
    }

    private fun addDriver(
        id: String, name: String, surname: String, middleName: String, imageUrl: String, birthday: String,
        drivingLicenseNumber: String, drivingLicenseValidity: String, medicalCertificateValidity: String
    ) {
        try {
            if (!pattern.matcher(imageUrl).matches()) throw HttpFormatException()

            SingletonDriver.getListDriver().add(
                Driver(
                    id,
                    if (name == "") {
                        resources.getString(R.string.name)
                    } else name,
                    if (surname == "") {
                        resources.getString(R.string.surname)
                    } else surname,
                    middleName,
                    imageUrl,
                    birthday,
                    drivingLicenseNumber,
                    drivingLicenseValidity,
                    medicalCertificateValidity
                )
            )
            this.finish()
        } catch (hfe: HttpFormatException) {
            Toast.makeText(
                this,
                resources.getText(R.string.not_valid_url),
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    internal inner class HttpFormatException : Exception()
}