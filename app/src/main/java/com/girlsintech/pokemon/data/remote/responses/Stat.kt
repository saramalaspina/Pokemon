package com.girlsintech.pokemon.data.remote.responses

import com.girlsintech.pokemon.data.remote.responses.StatX

data class Stat(
    val base_stat: Int,
    val effort: Int,
    val stat: StatX
)