package com.girlsintech.pokemon.data.remote.responses

import com.girlsintech.pokemon.data.remote.responses.MoveX
import com.girlsintech.pokemon.data.remote.responses.VersionGroupDetail

data class Move(
    val move: MoveX,
    val version_group_details: List<VersionGroupDetail>
)