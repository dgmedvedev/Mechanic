package com.medvedev.presentation.ui.activities.cars

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.medvedev.mechanic.R
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.cars.CarEditFragment

class CarEditActivity : AppCompatActivity(), OnEditingFinishedListener {

    private var screenMode = MODE_UNKNOWN
    private var idCar: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_car_edit)

        parseIntent()

        if (savedInstanceState == null) {
            launchRightMode()
        }
    }

    override fun onEditingFinished() {
        finish()
    }

    private fun parseIntent() {
        if (!intent.hasExtra(EXTRA_SCREEN_MODE)) {
            throw java.lang.RuntimeException(getString(R.string.param_screen_mode_exception))
        }
        val mode = intent.getStringExtra(EXTRA_SCREEN_MODE)
        if (mode != MODE_ADD && mode != MODE_EDIT) {
            throw java.lang.RuntimeException(getString(R.string.screen_mode_exception) + mode)
        }
        screenMode = mode
        if (screenMode == MODE_EDIT) {
            if (!intent.hasExtra(EXTRA_ID_CAR)) {
                throw java.lang.RuntimeException(getString(R.string.param_id_car_exception))
            }
            idCar = intent.getStringExtra(EXTRA_ID_CAR)
        }

//        if (idCar == null) {
//            showToast(getString(R.string.id_null))
//            finish()
//        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchRightMode() {
        val fragment = when (screenMode) {
            MODE_ADD -> CarEditFragment.getInstanceCarAdd()
            MODE_EDIT -> CarEditFragment.getInstanceCarEdit(idCar)
            else -> throw java.lang.RuntimeException(
                getString(R.string.screen_mode_exception) + screenMode
            )
        }
        supportFragmentManager.beginTransaction()
            .replace(R.id.car_edit_container, fragment)
            .commit()
    }

    companion object {
        private const val EXTRA_SCREEN_MODE = "extra_screen_mode"
        private const val EXTRA_ID_CAR = "extra_id_car"
        private const val MODE_ADD = "mode_add"
        private const val MODE_EDIT = "mode_edit"
        private const val MODE_UNKNOWN = ""


        fun getIntentAddCar(
            context: Context
        ): Intent {
            val intent = Intent(context, CarEditActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_ADD)
            return intent
        }

        fun getIntentEditCar(
            context: Context,
            idCar: String? = System.currentTimeMillis().toString()
        ): Intent {
            val intent = Intent(context, CarEditActivity::class.java)
            intent.putExtra(EXTRA_SCREEN_MODE, MODE_EDIT)
            intent.putExtra(EXTRA_ID_CAR, idCar)
            return intent
        }
    }
}