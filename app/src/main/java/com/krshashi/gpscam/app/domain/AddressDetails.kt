package com.krshashi.gpscam.app.domain

data class AddressDetails(
    val addressLine1: String,
    val addressLine2: String,
    val latitude: Double,
    val longitude: Double,
    val timestamp: String,
)
