package com.krshashi.gpscam.app.utils

import android.content.Context
import android.content.Intent
import android.location.Address
import android.provider.Settings
import androidx.core.net.toUri

fun Context.navigateToAppSetting() = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
    .apply { data = "package:$packageName".toUri() }
    .let { startActivity(it) }

fun Context.navigateToAppGallery() = Intent(Intent.ACTION_VIEW)
    .apply {
        type = "image/*"
        /*addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)*/
    }
    .let { startActivity(it) }

// Returns country flag emoji if available else null
val Address.countryFlag: String?
    get() {
        if (countryCode.length != 2) return null

        val upperCaseCode = countryCode.uppercase()
        val firstChar = Character.codePointAt(upperCaseCode, 0) - 0x41 + 0x1F1E6
        val secondChar = Character.codePointAt(upperCaseCode, 1) - 0x41 + 0x1F1E6

        return String(Character.toChars(firstChar)) + String(Character.toChars(secondChar))
    }