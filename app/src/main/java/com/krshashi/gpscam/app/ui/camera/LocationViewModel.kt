package com.krshashi.gpscam.app.ui.camera

import android.location.Address
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.model.LatLng
import com.krshashi.gpscam.app.domain.AddressDetails
import com.krshashi.gpscam.app.utils.DatetimeHelper
import com.krshashi.gpscam.app.utils.LocationHelper
import com.krshashi.gpscam.app.utils.ResultState
import com.krshashi.gpscam.app.utils.countryFlag
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationViewModel @Inject constructor(
    private val fusedLocationClient: FusedLocationProviderClient,
    private val locationHelper: LocationHelper
) : ViewModel() {
    private val _appResultFlow = MutableStateFlow<ResultState<AddressDetails>>(
        ResultState.Loading()
    )
    val appResultFlow = _appResultFlow.asStateFlow()


    @RequiresPermission(
        anyOf = [
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION"
        ]
    )
    fun getCurrentPosition() {
        viewModelScope.launch {
            _appResultFlow.value = ResultState.Loading(true)
            fusedLocationClient.getCurrentLocation(
                LocationHelper.Companion.currentLocationRequest,
                null
            )
                .addOnSuccessListener {
                    Log.d("LocationState", "Successful $it")
                    it?.let { decodeCoordinate(it.latitude, it.longitude) }
                }
                .addOnFailureListener {
                    Log.d("LocationState", "failed")
                    _appResultFlow.value = ResultState.Failure(it.message ?: "Something Wrong!")
                }
        }
    }

    fun decodeCoordinate(lat: Double, lng: Double) {
        viewModelScope.launch {
            locationHelper.reverseGeocode(
                latLng = LatLng(lat, lng),
                onResult = { address ->
                    val details = AddressDetails(
                        addressLine1 = address.addressLineOne(),
                        addressLine2 = address.addressLineTwo(),
                        latitude = address.latitude,
                        longitude = address.longitude,
                        timestamp = DatetimeHelper.getCurrentDateTime()
                    )

                    Log.d("LocationState", "decoding coordinates $details")
                    _appResultFlow.value = ResultState.Success(details)
                },
                onFailure = {
                    Log.d("LocationState", "decoding coordinates failed")
                    _appResultFlow.value = ResultState.Failure(it ?: "Something Wrong!")
                }
            )
        }
    }

    private fun Address.addressLineOne(): String {
        return "${locality}, ${adminArea}, $countryName $countryFlag"
    }

    private fun Address.addressLineTwo(): String {
        return try {
            getAddressLine(0)
        } catch (e: NullPointerException) {
            "${locality}, ${adminArea}, $countryName"
        }
    }
}