package com.girlsintech.pokemon.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.data.remote.responses.Stat
import com.girlsintech.pokemon.data.remote.responses.Type
import com.girlsintech.pokemon.ui.theme.*
import java.util.*


fun parseTypeToColor(pokemon: PokemonInfo, type: Type): Color {
    return when(type.type.name.lowercase(Locale.ROOT)) {
        "normal" -> Color.Green
        "fire" -> Color.Red
        "water" -> Color.Cyan
        "electric" -> Color.Yellow
        "grass" -> Color.Green
        "ice" -> Color.Blue
        "fighting" -> Color.Red
        "poison" -> Color.DarkGray
        "ground" -> Color.LightGray
        "flying" -> Color.Blue
        "psychic" -> Color.Green
        "bug" -> Color.Magenta
        "rock" -> Color.DarkGray
        "ghost" -> Color.Yellow
        "dragon" -> Color.Red
        "dark" -> Color.DarkGray
        "steel" -> Color.Red
        "fairy" -> Color.Magenta
        else -> Color.Black
    }
}

fun parseStatToColor(stat: Stat): Color {
    return when(stat.stat.name.lowercase(Locale.ROOT)) {
        "hp" -> hpColor.copy(0.8f)
        "attack" -> attackColor.copy(0.8f)
        "defense" -> defenseColor.copy(0.8f)
        "special-attack" -> specialAttackColor.copy(0.8f)
        "special-defense" -> specialDefenseColor.copy(0.8f)
        "speed" -> speedColor.copy(0.8f)
        else -> Color.White
    }
}

@Composable
fun parseStatToAbbr(stat: Stat): String {
    return when(stat.stat.name.lowercase(Locale.ROOT)) {
        "hp" -> stringResource(id = R.string.hp)
        "attack" -> stringResource(id = R.string.attack)
        "defense" -> stringResource(id = R.string.defense)
        "special-attack" -> stringResource(id = R.string.special_attack)
        "special-defense" -> stringResource(id = R.string.special_defense)
        "speed" -> stringResource(id = R.string.speed)
        else -> ""
    }
}

@Composable
fun parseType(type: String): String {
    return when(type.lowercase(Locale.ROOT)) {
        "normal" -> stringResource(id = R.string.normal)
        "fire" -> stringResource(id = R.string.fire)
        "fighting" -> stringResource(id = R.string.fighting)
        "water" -> stringResource(id = R.string.water)
        "flying" -> stringResource(id = R.string.flying)
        "grass" -> stringResource(id = R.string.grass)
        "poison" -> stringResource(id = R.string.poison)
        "electric" -> stringResource(id = R.string.electric)
        "ground" -> stringResource(id = R.string.ground)
        "psychic" -> stringResource(id = R.string.psychic)
        "rock" -> stringResource(id = R.string.rock)
        "ice" -> stringResource(id = R.string.ice)
        "bug" -> stringResource(id = R.string.bug)
        "dragon" -> stringResource(id = R.string.dragon)
        "ghost" -> stringResource(id = R.string.ghost)
        "dark" -> stringResource(id = R.string.dark)
        "steel" -> stringResource(id = R.string.steel)
        "fairy" -> stringResource(id = R.string.fairy)
        else -> ""
    }
}

fun parseTypeIt(type: String): String {
    return when(type.lowercase(Locale.ROOT)) {
        "normale" -> "Normal"
        "fuoco" -> "Fire"
        "lotta" -> "Fighting"
        "acqua" -> "Water"
        "volante" -> "Flying"
        "erba" -> "Grass"
        "veleno" -> "Poison"
        "elettro" -> "Electric"
        "terra" -> "Ground"
        "psico" -> "Psychic"
        "roccia" -> "Rock"
        "ghiaccio" -> "Ice"
        "coleottero" -> "Bug"
        "drago" -> "Dragon"
        "spettro" -> "Ghost"
        "buio" -> "Dark"
        "acciaio" -> "Steel"
        "folletto" -> "Fairy"
        else -> ""
    }
}