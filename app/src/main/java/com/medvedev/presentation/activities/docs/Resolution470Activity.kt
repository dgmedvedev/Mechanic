package com.medvedev.presentation.activities.docs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebViewClient
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityResolution141Binding

class Resolution470Activity : Activity() {

    private val binding by lazy {
        ActivityResolution141Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadPdfDoc()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPdfDoc() {
        val url = getString(R.string.url_resolution_470)

        binding.resolution141WebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true // привели страницу к моб. версии
            loadUrl(url)
            settings.javaScriptCanOpenWindowsAutomatically =
                true // привели страницу к моб. версии
        }
    }
}