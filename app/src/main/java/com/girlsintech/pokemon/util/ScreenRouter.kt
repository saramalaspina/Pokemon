package com.girlsintech.pokemon.util

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.viewmodel.PokemonViewModel

object ScreenRouter {
    var currentScreen: MutableState<Int> = mutableStateOf(1)
    var previousScreen: MutableState<Int> = mutableStateOf(1)

    var color: MutableState<Color> = mutableStateOf(Color.White)
    var pokemonSelected: MutableState<Pokemon?> = mutableStateOf(null)
    var viewModel: MutableState<PokemonViewModel?> = mutableStateOf(null)
    fun navigateTo(source: Int = currentScreen.value, destination: Int) {
        previousScreen.value = source
        currentScreen.value = destination
    }

    fun navigateToDetail(source: Int = currentScreen.value, dominantColor: Color, pokemon: Pokemon, viewModelDb: PokemonViewModel) {
        previousScreen.value = source
        currentScreen.value = 3
        color.value = dominantColor
        pokemonSelected.value = pokemon
        viewModel.value = viewModelDb
    }

    fun navigateToStats(source: Int = currentScreen.value, dominantColor: Color, pokemon: Pokemon, viewModelDb: PokemonViewModel){
        previousScreen.value = source
        currentScreen.value = 4
        color.value = dominantColor
        pokemonSelected.value = pokemon
        viewModel.value = viewModelDb
    }
}