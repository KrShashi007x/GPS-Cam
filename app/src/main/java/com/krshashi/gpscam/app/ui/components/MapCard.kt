package com.krshashi.gpscam.app.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.GoogleMapOptions
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.ComposeMapColorScheme
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.krshashi.gpscam.app.ui.theme.AppIcons

@Composable
fun MapView(
    latlng: LatLng,
    modifier: Modifier = Modifier,
    shape: Shape = MaterialTheme.shapes.medium,
    zoom: Float = 18f,
) {
    val mapUiSettings = remember {
        MapUiSettings(
            compassEnabled = false,
            zoomControlsEnabled = false,
            tiltGesturesEnabled = false,
            rotationGesturesEnabled = false
        )
    }

    val mapProperties = remember {
        MapProperties(mapType = MapType.SATELLITE)
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(latlng, zoom)
    }

    Box(modifier = modifier.clip(shape), contentAlignment = Alignment.Center) {
        GoogleMap(
            cameraPositionState = cameraPositionState,
            modifier = Modifier.fillMaxSize(),
            mapColorScheme = ComposeMapColorScheme.LIGHT,
            onMyLocationButtonClick = { true },
            uiSettings = mapUiSettings,
            properties = mapProperties
        )

        Icon(
            painter = painterResource(AppIcons.Pin),
            contentDescription = "pin_icon",
            modifier = Modifier.size(32.dp),
            tint = Color(0xFFF34031)
        )
    }
}