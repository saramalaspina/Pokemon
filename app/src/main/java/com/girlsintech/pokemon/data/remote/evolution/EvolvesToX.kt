package com.girlsintech.pokemon.data.remote.evolution

data class EvolvesToX(
    val evolution_details: List<EvolutionDetailX>,
    val evolves_to: List<Any>,
    val is_baby: Boolean,
    val species: SpeciesXX
)