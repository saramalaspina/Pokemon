package com.girlsintech.pokemon.util

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import com.girlsintech.pokemon.screens.fontBasic
import java.util.*

@Composable
fun TextInfo(
    text : String,
    color: Color = Color.Gray
){
    Text(
        text = text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
        fontSize = 15.sp,
        fontFamily = fontBasic(),
        color = color
    )
}