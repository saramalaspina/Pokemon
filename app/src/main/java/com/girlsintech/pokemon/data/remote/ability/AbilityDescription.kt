package com.girlsintech.pokemon.data.remote.ability

data class AbilityDescription(
    val effect_changes: List<Any>,
    val effect_entries: List<EffectEntry>,
    val flavor_text_entries: List<FlavorTextEntry>,
    val generation: Generation,
    val id: Int,
    val is_main_series: Boolean,
    val name: String,
    val names: List<Name>,
    val pokemon: List<Pokemon>
)