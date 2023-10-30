package com.medvedev.presentation.ui.activities.drivers

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.medvedev.mechanic.R
import com.medvedev.presentation.ui.OnEditingFinishedListener
import com.medvedev.presentation.ui.fragments.drivers.DriverEditFragment

class DriverEditActivity : AppCompatActivity(), OnEditingFinishedListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_edit)

        if (savedInstanceState == null) {
            launchFragment()
        }
    }

    override fun onEditingFinished() {
        finish()
    }

    private fun launchFragment() {
        supportFragmentManager.beginTransaction()
            .replace(R.id.driver_container, DriverEditFragment.getInstanceDriverAdd())
            .commit()
    }

    companion object {
        fun getIntentAddDriver(context: Context): Intent {
            return Intent(context, DriverEditActivity::class.java)
        }
    }
}