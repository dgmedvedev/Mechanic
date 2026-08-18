package com.medvedev.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.medvedev.presentation.navigation.MechanicNavGraph
import com.medvedev.presentation.theme.MechanicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MechanicTheme {
                val navController = rememberNavController()
                MechanicNavGraph(
                    navController = navController,
                    modifier = Modifier.Companion.fillMaxSize(),
                )
            }
        }
    }
}