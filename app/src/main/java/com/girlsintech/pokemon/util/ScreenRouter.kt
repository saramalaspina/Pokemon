package com.girlsintech.pokemon.util

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color

object ScreenRouter {
    var currentScreen: MutableState<Int> = mutableStateOf(1)
    var previousScreen: MutableState<Int> = mutableStateOf(1)

    var color: MutableState<Color> = mutableStateOf(Color.White)
    var url: MutableState<String> = mutableStateOf("")
    var imgUrl: MutableState<String> = mutableStateOf("")


    fun navigateTo(source: Int = currentScreen.value, destination: Int) {
        previousScreen.value = source
        currentScreen.value = destination
    }

    fun navigateToDetail(source: Int = currentScreen.value, dominantColor: Color, pokemonUrl: String, img: String) {
        previousScreen.value = source
        currentScreen.value = 3
        color.value = dominantColor
        url.value = pokemonUrl
        imgUrl.value = img
    }
}