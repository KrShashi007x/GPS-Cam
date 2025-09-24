package com.krshashi.gpscam.app.ui.camera

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel

@Composable
fun CameraScreen(
    onNavigateUp:() -> Unit,
    onNavigateToGallery:() -> Unit,
    modifier: Modifier = Modifier,
    viewModel: CameraViewModel = hiltViewModel()
) {
    CameraContents(modifier = modifier)
}

@Composable
private fun CameraContents(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Camera Screen")
    }
}