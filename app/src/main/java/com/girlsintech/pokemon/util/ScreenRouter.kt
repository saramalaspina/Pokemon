package com.girlsintech.pokemon.util

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.girlsintech.pokemon.db.Pokemon

object ScreenRouter {
    var currentScreen: MutableState<Int> = mutableStateOf(1)
    var previousScreen: MutableState<Int> = mutableStateOf(1)

    var color: MutableState<Color> = mutableStateOf(Color.White)

    var pokemonSelected: MutableState<Pokemon?> = mutableStateOf(null)
    fun navigateTo(source: Int = currentScreen.value, destination: Int) {
        previousScreen.value = source
        currentScreen.value = destination
    }

    fun navigateToDetail(source: Int = currentScreen.value, dominantColor: Color, pokemon: Pokemon) {
        previousScreen.value = source
        currentScreen.value = 3
        color.value = dominantColor
        pokemonSelected.value = pokemon
    }
}