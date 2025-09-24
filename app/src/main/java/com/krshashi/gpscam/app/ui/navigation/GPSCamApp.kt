package com.krshashi.gpscam.app.ui.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.krshashi.gpscam.app.ui.permissions.PermissionsRoute

@Composable
fun GPSCamApp(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Any = PermissionsRoute
) {
    Scaffold(modifier = modifier) { innerPadding ->
        RootNavHost(
            navController = navController,
            startDestination = startDestination,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}