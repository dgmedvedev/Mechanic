package com.medvedev.presentation.activities.docs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.medvedev.mechanic.databinding.ActivityDocsNormativeBinding

class NormativeDocsActivity : Activity() {

    private val binding by lazy {
        ActivityDocsNormativeBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.norms.setOnClickListener {
            launchActivity(FuelConsumptionRates())
        }

        binding.resolution141.setOnClickListener {
            launchActivity(Resolution470Activity())
        }
    }

    private fun launchActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }

    companion object {
        const val BASE_URL = "https://drive.google.com/viewerng/viewer?embedded=true&url="
    }
}