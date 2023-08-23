package com.girlsintech.pokemon.util

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.responses.Stat
import com.girlsintech.pokemon.ui.theme.*
import java.util.*

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

fun parseGeneration(number: String): Int {
    return when(number){
        "I" -> 1
        "II" -> 2
        "III" -> 3
        "IV" -> 4
        "V" -> 5
        "VI" -> 6
        "VII" -> 7
        "VIII" -> 8
        "IX" -> 9
        else -> 0
    }
}

fun parseGenerationFromInt(number: Int): String {
    return when(number){
        1 -> "I"
        2-> "II"
        3-> "III"
        4-> "IV"
        5 -> "V"
        6 -> "VI"
        7 -> "VII"
        8 -> "VIII"
        9 -> "IX"
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

@Composable
fun parseEggGroups(eggGroup: String): String {
    return when(eggGroup.lowercase(Locale.ROOT)) {
        "monster" -> stringResource(id = R.string.monster)
        "water1" -> stringResource(id = R.string.water1)
        "water2" -> stringResource(id = R.string.water2)
        "water3" -> stringResource(id = R.string.water3)
        "flying" -> stringResource(id = R.string.flying)
        "bug" -> stringResource(id = R.string.bug)
        "ground" -> stringResource(id = R.string.ground)
        "fairy" -> stringResource(id = R.string.magic)
        "plant" -> stringResource(id = R.string.plant)
        "humanshape" -> stringResource(id = R.string.human)
        "mineral" -> stringResource(id = R.string.mineral)
        "indeterminate" -> stringResource(id = R.string.indeterminate_egg)
        "ditto" -> stringResource(id = R.string.ditto)
        "dragon" -> stringResource(id = R.string.dragon)
        "no-eggs" -> stringResource(id = R.string.no_eggs)
        else -> ""
    }
}

fun parseTypeIt(type: String): String {
    return when(type.lowercase(Locale.ROOT)) {
        "nessuno" -> "None"
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

@Composable
fun parseGrowthRate(growthRate: String): String {
    return when(growthRate.lowercase(Locale.ROOT)){
        "fast" -> stringResource(id = R.string.fast)
        "medium-fast" -> stringResource(id = R.string.medium_fast)
        "medium" -> stringResource(id = R.string.medium)
        "medium-slow" -> stringResource(id = R.string.medium_slow)
        "slow" -> stringResource(id = R.string.slow)
        "fast-then-very-slow" -> stringResource(id = R.string.fast_then_vey_slow)
        "slow-then-very-fast" -> stringResource(id = R.string.slow_then_very_fast)
        else -> ""
    }
}
