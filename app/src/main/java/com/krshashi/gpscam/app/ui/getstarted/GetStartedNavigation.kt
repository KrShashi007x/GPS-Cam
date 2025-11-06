package com.krshashi.gpscam.app.ui.getstarted

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PermissionsRoute

fun NavController.navigateToGetStarted() {
    this.navigate(PermissionsRoute)
}

fun NavGraphBuilder.getStartedScreen(
    onNavigateUp: () -> Unit,
    onNavigateToCamera: () -> Unit,
    onNavToAppSetting: () -> Unit
) {
    composable<PermissionsRoute> {
        GetStartedScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToCamera = onNavigateToCamera,
            onNavToAppSetting = onNavToAppSetting,
            modifier = Modifier.fillMaxSize()
        )
    }
}