package com.krshashi.gpscam.app.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.krshashi.gpscam.app.ui.theme.AppIcons
import com.krshashi.gpscam.app.ui.theme.GPSCamTheme

@Composable
fun AddressInfoCard(
    addressLine1: String,
    addressLine2: String,
    coordinates: String,
    datetime: String,
    modifier: Modifier = Modifier,
    contentColor: Color = Color.White,
    background: Color = Color.Black.copy(0.5f),
    contentPadding: PaddingValues = PaddingValues(16.dp, 8.dp)
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = background,
            contentColor = contentColor
        )
    ) {
        Column(
            Modifier
                .wrapContentHeight()
                .padding(contentPadding),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = addressLine1,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(Modifier.height(4.dp))

            Text(
                text = addressLine2,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = coordinates,
                style = MaterialTheme.typography.bodySmall,
            )
            Text(
                text = datetime,
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Preview
@Composable
private fun AddressInfoCardPreview() {
    GPSCamTheme {
        AddressInfoCard(
            addressLine1 = "Solea, Jharkhand, India \uD83C\uDDEE\uD83C\uDDF3",
            addressLine2 = "J779+786, Solea, Jharkhand 829119, India",
            coordinates = "Lat 23.613634 Long 85.26743",
            datetime = "15/09/2025 04:20 PM GMT +05:30"
        )
    }
}

@Composable
fun ErrorCard(
    message: String,
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
            Icon(
                painter = painterResource(AppIcons.Error),
                contentDescription = "error_icon",
                modifier = Modifier.size(ButtonDefaults.IconSize)
            )
            Text(text = message)
        }
    }
}