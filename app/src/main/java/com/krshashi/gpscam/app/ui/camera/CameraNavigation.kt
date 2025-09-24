package com.krshashi.gpscam.app.ui.camera

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object CameraRoute

fun NavHostController.navigateToCamera() {
    this.navigate(CameraRoute)
}

fun NavGraphBuilder.cameraScreen(
    onNavigateUp: () -> Unit,
    onNavigateToGallery: () -> Unit
) {
    composable<CameraRoute> {
        CameraScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToGallery = onNavigateToGallery,
            modifier = Modifier.fillMaxSize()
        )
    }
}