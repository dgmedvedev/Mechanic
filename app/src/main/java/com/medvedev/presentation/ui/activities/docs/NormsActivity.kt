package com.medvedev.presentation.ui.activities.docs

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.medvedev.mechanic.R
import com.medvedev.mechanic.databinding.ActivityNormsBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NormsActivity : AppCompatActivity() {

    private val binding by lazy {
        ActivityNormsBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        loadPdfDoc()
    }

    @SuppressLint("SetJavaScriptEnabled")
    private fun loadPdfDoc() {
        val url = getString(R.string.url_norms)

        binding.webViewNorms.apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true // привели страницу к моб. версии
            loadUrl(url)
            settings.javaScriptCanOpenWindowsAutomatically = true // привели страницу к моб. версии
        }
    }
}