package com.medvedev.mechanic.activity.docs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebViewClient
import com.medvedev.mechanic.databinding.ActivityResolution3Binding

class Resolution3Activity : Activity() {

    private val binding by lazy {
        ActivityResolution3Binding.inflate(layoutInflater)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val pdf = "http://www.centr-cen.by/upload/normy3.pdf"

        binding.resolution44WebView.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true // привели страницу к моб. версии
            loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")
            settings.javaScriptCanOpenWindowsAutomatically = true // привели страницу к моб. версии
        }
    }
}