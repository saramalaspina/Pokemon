package com.girlsintech.pokemon.util

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import com.girlsintech.pokemon.data.remote.ability.AbilityDescription
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.viewmodel.PokemonViewModel

object SelectedPokemon {
    var color: MutableState<Color> = mutableStateOf(Color.White)
    var pokemonSelected: MutableState<Pokemon?> = mutableStateOf(null)

    fun selectPokemon(dominantColor: Color, pokemon: Pokemon){
        color.value = dominantColor
        pokemonSelected.value = pokemon
    }

}