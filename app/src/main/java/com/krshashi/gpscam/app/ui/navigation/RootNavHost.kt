package com.krshashi.gpscam.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.krshashi.gpscam.app.ui.camera.cameraScreen
import com.krshashi.gpscam.app.ui.camera.navigateToCamera
import com.krshashi.gpscam.app.ui.permissions.PermissionsRoute
import com.krshashi.gpscam.app.ui.permissions.permissionsScreen

@Composable
fun RootNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    startDestination: Any = PermissionsRoute
) {
    NavHost(
        navController = navController,
        modifier = modifier,
        startDestination = startDestination
    ) {
        permissionsScreen(
            onNavigateUp = navController::popBackStack,
            onNavigateToCamera = navController::navigateToCamera
        )

        cameraScreen(
            onNavigateUp = navController::navigateUp,
            onNavigateToGallery = { /*TODO*/ }
        )
    }
}