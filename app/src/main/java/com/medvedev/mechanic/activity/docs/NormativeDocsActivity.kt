package com.medvedev.mechanic.activity.docs

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.medvedev.mechanic.R

class NormativeDocsActivity : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_docs_normative)


        findViewById<Button>(R.id.resolution44)
            .setOnClickListener {
                start(Resolution3Activity())
            }
        findViewById<Button>(R.id.resolution141)
            .setOnClickListener {
                start(Resolution141Activity())
            }
    }

    private fun start(activity: Activity) {
        startActivity(Intent(this, activity::class.java))
    }
}