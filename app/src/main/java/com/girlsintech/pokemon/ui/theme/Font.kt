package com.girlsintech.pokemon.screens

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily

@Composable
fun fontPokemon() : FontFamily {
    val assets = LocalContext.current.assets
    return FontFamily(
        Font("Candal-Regular.ttf", assets)
    )
}
@Composable
fun fontBasic() : FontFamily {
    val assets = LocalContext.current.assets
    return FontFamily(
        Font("Nunito.ttf", assets)
    )
}
