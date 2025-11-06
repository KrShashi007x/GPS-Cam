package com.krshashi.gpscam.app.ui.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.krshashi.gpscam.app.ui.camera.LocationViewModel
import com.krshashi.gpscam.app.ui.camera.CameraRoute
import com.krshashi.gpscam.app.ui.getstarted.PermissionsRoute
import com.krshashi.gpscam.app.utils.rememberAppPermissionsState


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun GPSCamApp(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    locationViewModel: LocationViewModel = hiltViewModel(),
    appPermissionsState: MultiplePermissionsState = rememberAppPermissionsState()
) {
    Scaffold(modifier = modifier) { innerPadding ->
        RootNavHost(
            navController = navController,
            startDestination = when {
                appPermissionsState.allPermissionsGranted -> {
                    @SuppressLint("MissingPermission")
                    locationViewModel.getCurrentPosition()
                    CameraRoute
                }

                else -> PermissionsRoute
            },
            locationViewModel = locationViewModel,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        )
    }
}