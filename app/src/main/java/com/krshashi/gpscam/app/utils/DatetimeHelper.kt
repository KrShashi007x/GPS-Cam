package com.krshashi.gpscam.app.utils

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter


object DatetimeHelper {
    private const val OUTPUT_DATE_TIME_PATTERN = "dd/MM/yyyy hh:mm a 'GMT' XXX"

    fun getCurrentDateTime(): String {
        val millis = System.currentTimeMillis()
        val zoneId = ZoneId.systemDefault()
        val dateTime = Instant.ofEpochMilli(millis).atZone(zoneId)
        val formatter = DateTimeFormatter.ofPattern(OUTPUT_DATE_TIME_PATTERN)

        return dateTime.format(formatter)
    }
}