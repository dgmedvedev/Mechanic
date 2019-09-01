package com.medvedev.mechanic.activity.cars

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.medvedev.mechanic.R
import kotlinx.android.synthetic.main.activity_details_car.*

class CarDetailsActivity : Activity() {

    companion object {
        private const val ID_CAR = "ID_CAR"

        fun getIntent(context: Context, idCar: String): Intent {
            val intent = Intent(context, CarDetailsActivity::class.java)
            intent.putExtra(ID_CAR, idCar)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_details_car)

        val idCar = intent.getStringExtra(ID_CAR)
        val user: Car? = SingletonCar.getCarById(idCar)
        if (user == null) {
            Toast.makeText(
                this,
                resources.getText(R.string.id_not_found),
                Toast.LENGTH_SHORT
            ).show()
            this.finish()
        }

        user?.run {
            brandTextView.text = user.brand
            modelTextView.text = user.model
            yearProductionTextView.text = user.yearProduction.toString()
            stateNumberTextView.text = user.stateNumber
            bodyNumberTextView.text = user.bodyNumber
            engineDisplacementTextView.text = user.engineDisplacement
            fuelTypeTextView.text = user.fuelType
            allowableWeightTextView.text = user.allowableWeight
            technicalPassportTextView.text = user.technicalPassport
            checkupTextView.text = user.checkup
            insuranceTextView.text = user.insurance
            hullInsuranceTextView.text = user.hullInsurance
        }

        delete.setOnClickListener {
            SingletonCar.getListCar().remove(user)
            this.finish()
        }

        edit.setOnClickListener {
            startActivity(CarEditActivity.getIntent(this@CarDetailsActivity, idCar))
            this.finish()
        }
    }
}