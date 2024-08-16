package com.medvedev.presentation.ui.activities.docs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.medvedev.mechanic.databinding.ActivityNormativeDocsBinding

class NormativeDocsActivity : Activity() {

    private val binding by lazy {
        ActivityNormativeDocsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setListeners()
    }

    private fun setListeners() {
        binding.norms.setOnClickListener {
            launchActivity(NormsActivity())
        }

        binding.resolution470.setOnClickListener {
            launchActivity(Resolution470Activity())
        }
    }

    private fun launchActivity(activity: Activity) {
        val intent = Intent(this, activity::class.java)
        startActivity(intent)
    }
}