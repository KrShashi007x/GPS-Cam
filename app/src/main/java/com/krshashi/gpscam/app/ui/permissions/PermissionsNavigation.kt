package com.krshashi.gpscam.app.ui.permissions

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import kotlinx.serialization.Serializable

@Serializable
object PermissionsRoute

fun NavController.navigateToPermissions() {
    this.navigate(PermissionsRoute)
}

fun NavGraphBuilder.permissionsScreen(
    onNavigateUp: () -> Unit,
    onNavigateToCamera: () -> Unit,
) {
    composable<PermissionsRoute> {
        PermissionScreen(
            onNavigateUp = onNavigateUp,
            onNavigateToCamera = onNavigateToCamera,
            modifier = Modifier.fillMaxSize()
        )
    }
}