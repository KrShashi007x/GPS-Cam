package com.krshashi.gpscam.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krshashi.gpscam.app.ui.theme.GPSCamTheme

@Composable
fun CaptureButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
    borderStroke: BorderStroke = BorderStroke(4.dp, Color.Black),
    shape: Shape = CircleShape,
    indicateProgress: Boolean = false
) {
    Box(modifier) {
        Box(
            Modifier
                .fillMaxSize()
                .clip(shape)
                .border(borderStroke, shape)
                .padding(4.dp)
                .background(contentColor, shape)
                .clickable(onClick = { if (!indicateProgress) onClick() })

        )

        if (indicateProgress) CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(),
            trackColor = Color.White
        )
    }
}

@Preview
@Composable
fun CaptureButtonPreview() {
    GPSCamTheme {
        CaptureButton(
            onClick = {},
            modifier = Modifier
                .padding(16.dp)
                .size(56.dp)
        )
    }
}