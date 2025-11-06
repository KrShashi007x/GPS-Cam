package com.krshashi.gpscam.app.utils

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class LocationHelper @Inject constructor(
    private val geocoder: Geocoder
) {
    suspend fun reverseGeocode(
        latLng: LatLng,
        onResult: (Address) -> Unit,
        onFailure: (String?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            asyncDecoder(
                coordinate = latLng,
                onResult = { onResult(it[0]) },
                onFailure = onFailure
            )
        } else {
            syncDecoder(
                coordinate = latLng,
                onResult = { onResult(it[0]) },
                onFailure = onFailure
            )
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun asyncDecoder(
        coordinate: LatLng,
        onResult: (List<Address>) -> Unit,
        onFailure: (err: String?) -> Unit
    ) = try {
        geocoder.getFromLocation(
            coordinate.latitude,
            coordinate.longitude,
            MAX_GEOCODER_RESULTS
        ) { onResult(it) }
    } catch (e: Exception) {
        onFailure(e.message)
    }

    @Suppress("DEPRECATION")
    private suspend fun syncDecoder(
        coordinate: LatLng,
        onResult: (List<Address>) -> Unit,
        onFailure: (err: String?) -> Unit
    ) = withContext(Dispatchers.IO) {
        try {
            geocoder.getFromLocation(
                coordinate.latitude,
                coordinate.longitude,
                MAX_GEOCODER_RESULTS
            )?.let(onResult)
        } catch (e: Exception) {
            onFailure(e.message)
        }
    }

    companion object {
        private const val MAX_GEOCODER_RESULTS: Int = 1

        val currentLocationRequest = CurrentLocationRequest
            .Builder()
            .setPriority(PRIORITY_HIGH_ACCURACY)
            .build()
    }
}