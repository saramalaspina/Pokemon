package com.girlsintech.pokemon.data.models

data class PokemonItem(
    val pokemonName: String,
    val imageUrl: String,
    val number: Int,
    val favourite: Boolean,
    val type: String?,
)

class PokemonList {
    var pokemonList: MutableList<PokemonItem> = mutableListOf()
}
