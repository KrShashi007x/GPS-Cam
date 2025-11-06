package com.krshashi.gpscam.app.ui.theme

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.krshashi.gpscam.app.R

val FontFamily.Companion.Figtree: FontFamily
    get() = FontFamily(
        Font(R.font.figtree_medium, FontWeight.Medium),
        Font(R.font.figtree_medium, FontWeight.Normal),
        Font(R.font.figtree_bold, FontWeight.Bold),
    )