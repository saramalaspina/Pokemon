package com.girlsintech.pokemon.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

@Composable
fun fontFamily() : FontFamily {
    val assets = LocalContext.current.assets
    return FontFamily(
        Font("Candal-Regular.ttf", assets)
    )
}