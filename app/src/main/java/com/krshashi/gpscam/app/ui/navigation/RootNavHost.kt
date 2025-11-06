package com.krshashi.gpscam.app.ui.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.krshashi.gpscam.app.ui.camera.CameraRoute
import com.krshashi.gpscam.app.ui.camera.LocationViewModel
import com.krshashi.gpscam.app.ui.camera.cameraScreen
import com.krshashi.gpscam.app.ui.camera.navigateToCamera
import com.krshashi.gpscam.app.ui.getstarted.PermissionsRoute
import com.krshashi.gpscam.app.ui.getstarted.getStartedScreen
import com.krshashi.gpscam.app.utils.navigateToAppGallery
import com.krshashi.gpscam.app.utils.navigateToAppSetting

@Composable
fun RootNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Any = CameraRoute,
    context: Context = LocalContext.current,
    locationViewModel: LocationViewModel
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        getStartedScreen(
            onNavigateUp = navController::popBackStack,
            onNavigateToCamera = navController::navigateToCamera,
            onNavToAppSetting = { context.navigateToAppSetting() }
        )

        cameraScreen(
            onNavigateToGallery = { context.navigateToAppGallery() },
            locationViewModel = locationViewModel
        )
    }
}