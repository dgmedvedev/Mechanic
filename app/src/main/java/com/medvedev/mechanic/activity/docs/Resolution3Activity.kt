package com.medvedev.mechanic.activity.docs

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.os.Handler
import android.webkit.WebView
import android.webkit.WebViewClient
import com.medvedev.mechanic.R

class Resolution3Activity : Activity() {
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resolution3)

        val pdf = "http://www.centr-cen.by/upload/normy3.pdf"

        val webView = findViewById<WebView>(R.id.resolution44WebView)

        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true // привели страницу к моб. версии
        webView.loadUrl("http://drive.google.com/viewerng/viewer?embedded=true&url=$pdf")
        webView.settings.javaScriptCanOpenWindowsAutomatically = true // привели страницу к моб. версии
    }
}