package com.krshashi.gpscam.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.krshashi.gpscam.app.ui.navigation.GPSCamApp
import com.krshashi.gpscam.app.ui.theme.GPSCamTheme
import com.krshashi.gpscam.app.utils.enableEdgeToEdgeLight
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdgeLight()
        setAppContent()
    }

    private fun setAppContent() {
        setContent {
            GPSCamTheme(darkTheme = false) {
                GPSCamApp(
                    navController = rememberNavController(),
                    modifier = Modifier.fillMaxSize()
                )
            }
        }
    }
}