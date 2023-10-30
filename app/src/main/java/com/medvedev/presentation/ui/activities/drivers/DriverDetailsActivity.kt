package com.medvedev.presentation.ui.activities.drivers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.medvedev.mechanic.R
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.drivers.DriverDetailsFragment

class DriverDetailsActivity : AppCompatActivity(), OnEditingFinishedListener {

    private var idDriver: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_details)

        parseIntent()

        if (savedInstanceState == null) {
            idDriver?.let {
                launchFragment(DriverDetailsFragment.getInstance(it))
            }
        }
    }

    override fun onEditingFinished() {
        finish()
    }

    private fun parseIntent() {
        idDriver = intent.getStringExtra(ID_DRIVER)

        if (idDriver == null) {
            showToast(getString(R.string.id_null))
            finish()
        }
    }

    private fun showToast(message: CharSequence) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.driver_container, fragment)
            .commit()
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