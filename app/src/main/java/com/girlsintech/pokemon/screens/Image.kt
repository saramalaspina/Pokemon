package com.girlsintech.pokemon.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

@Composable
fun myImage(
    idImage: Int
) {
    Image(painter = painterResource(id = idImage),
        contentDescription = "image",
        modifier = Modifier
            .fillMaxWidth()
    )
}