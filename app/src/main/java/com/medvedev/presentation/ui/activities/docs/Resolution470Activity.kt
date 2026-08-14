package com.medvedev.presentation.ui.activities.docs

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityResolution470Binding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Resolution470Activity : AppCompatActivity() {

    private val binding by lazy {
        ActivityResolution470Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadPdfDoc()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPdfDoc() {
        val url = getString(R.string.url_resolution_470)

        binding.webViewResolution470.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true // привели страницу к моб. версии
            loadUrl(url)
            settings.javaScriptCanOpenWindowsAutomatically =
                true // привели страницу к моб. версии
        }
    }
}