package com.medvedev.mechanic.activity.docs

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


        binding.resolution44.setOnClickListener {
            start(Resolution3Activity())
        }
        binding.resolution141.setOnClickListener {
            start(Resolution141Activity())
        }
    }

    private fun start(activity: Activity) {
        startActivity(Intent(this, activity::class.java))
    }
}