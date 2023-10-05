package com.medvedev.mechanic.activity.docs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebViewClient
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityResolution3Binding

class Resolution3Activity : Activity() {

    private val binding by lazy {
        ActivityResolution3Binding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadPdfDoc()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPdfDoc() {
        val url = getString(R.string.url_resolution_3)

        binding.resolution44WebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true // привели страницу к моб. версии
            loadUrl(url)
            settings.javaScriptCanOpenWindowsAutomatically = true // привели страницу к моб. версии
        }
    }
}