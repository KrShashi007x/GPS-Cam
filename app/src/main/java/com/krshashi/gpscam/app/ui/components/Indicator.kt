package com.krshashi.gpscam.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krshashi.gpscam.app.ui.theme.GPSCamTheme

@Composable
fun ProgressIndicator(
    label: String,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
    background: Color = Color.Black.copy(0.5f),
    contentPadding: PaddingValues = PaddingValues(16.dp, 8.dp)
) {
    Card(
        shape = CircleShape,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = contentColor
        )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.padding(contentPadding)
        ) {
            CircularProgressIndicator(
                modifier = Modifier.size(ButtonDefaults.IconSize),
                color = contentColor,
                strokeWidth = 2.dp
            )
            Text(text = label)
        }
    }
}

@Preview
@Composable
private fun ProgressIndicatorPreview() {
    GPSCamTheme {
        ProgressIndicator("Loading details")
    }
}