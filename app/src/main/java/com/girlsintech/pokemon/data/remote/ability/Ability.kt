package com.girlsintech.pokemon.data.remote.ability

data class Ability(
    val count: Int,
    val next: String,
    val previous: Any,
    val results: List<Result>
)