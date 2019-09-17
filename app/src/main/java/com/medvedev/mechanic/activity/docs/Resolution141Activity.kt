package com.medvedev.mechanic.activity.docs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import com.medvedev.mechanic.R

class Resolution141Activity : Activity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resolution141)

        val pdf = "http://www.dosaaf.gov.by/_modules/_cfiles/files/Normi_topliva.pdf"

        val webView = findViewById<WebView>(R.id.resolution141WebView)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true // привели страницу к моб. версии
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")
        webView.settings.javaScriptCanOpenWindowsAutomatically = true // привели страницу к моб. версии
    }
}