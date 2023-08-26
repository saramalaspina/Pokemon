package com.girlsintech.pokemon.screens.detailpage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.ability.AbilityDescription
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.data.remote.species.Species
import com.girlsintech.pokemon.screens.fontBasic
import com.girlsintech.pokemon.util.TextInfo
import com.girlsintech.pokemon.util.parseEggGroups
import com.girlsintech.pokemon.util.parseGrowthRate
import java.util.*
import kotlin.math.roundToInt

@Composable
fun PokemonDetailSection(
    dominantColor: Color,
    pokemonInfo: PokemonInfo,
    pokemonSpecies: Species,
    ability1: AbilityDescription?,
    ability2: AbilityDescription?,
    ability3: AbilityDescription?
) {
    val noSpecies = stringResource(id = R.string.no_available)
    var species by remember {
        mutableStateOf(noSpecies)
    }

    var isDialogShown by remember {
        mutableStateOf(false)
    }

    var abilityDescription by rememberSaveable {
        mutableStateOf("")
    }

    var ability: AbilityDescription? by remember {
        mutableStateOf(null)
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
            .verticalScroll(scrollState)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (detailCol1, detailCol2) = createRefs()

                Column(
                    modifier = Modifier.
                    constrainAs(detailCol1) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextInfo(text = stringResource(id = R.string.species))
                    TextInfo(text = stringResource(id = R.string.height))
                    TextInfo(text = stringResource(id = R.string.weight))
                    TextInfo(text = stringResource(id = R.string.abilities))
                }

                Column(
                    modifier = Modifier.
                    constrainAs(detailCol2) {
                        start.linkTo(parent.start, 130.dp)
                        top.linkTo(parent.top)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pokemonSpecies.genera.forEach {
                        if (it.language.name == Locale.getDefault().language) {
                            species = it.genus
                        }
                    }

                    TextInfo(text = species, Color.Black)

                    TextInfo(
                        text = "${(pokemonInfo.height * 100f).roundToInt() / 1000f} m",
                        Color.Black
                    )
                    TextInfo(
                        text = "${(pokemonInfo.weight * 100f).roundToInt() / 1000f} kg",
                        Color.Black
                    )

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ability1!!.names.forEach { name ->
                            if (name.language.name == Locale.getDefault().language) {
                                Text(
                                    text = name.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    },
                                    color = Color.Black,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 15.sp,
                                    fontFamily = fontBasic(),
                                    modifier = Modifier
                                        .clickable {
                                            isDialogShown = true
                                            ability = ability1
                                            abilityDescription = ability1.name
                                        }
                                )
                            }
                        }

                        ability2?.names?.forEach { name ->
                            if (name.language.name == Locale.getDefault().language) {
                                Text(
                                    text = name.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    },
                                    color = Color.Black,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 15.sp,
                                    fontFamily = fontBasic(),
                                    modifier = Modifier
                                        .clickable {
                                            isDialogShown = true
                                            ability = ability2
                                            abilityDescription = ability2.name
                                        }
                                )
                            }
                        }

                        ability3?.names?.forEach { name ->
                            if (name.language.name == Locale.getDefault().language) {
                                Text(
                                    text = name.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    },
                                    color = Color.Black,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 15.sp,
                                    fontFamily = fontBasic(),
                                    modifier = Modifier
                                        .clickable {
                                            isDialogShown = true
                                            ability = ability3
                                            abilityDescription = ability3.name
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (detailCol1, detailCol2) = createRefs()
                Column(
                    modifier = Modifier.
                    constrainAs(detailCol1) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top, 20.dp)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextInfo(text = stringResource(id = R.string.generation))
                    TextInfo(text = stringResource(id = R.string.growth_rate))
                    TextInfo(text = stringResource(id = R.string.egg_groups))
                }

                Column(
                    modifier = Modifier.constrainAs(detailCol2) {
                        start.linkTo(parent.start, 130.dp)
                        top.linkTo(parent.top, 20.dp)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    val gen = pokemonSpecies.generation.name.split("-")[1]
                    TextInfo(text = gen.uppercase(), Color.Black)

                    TextInfo(
                        text = parseGrowthRate(growthRate = (pokemonSpecies.growth_rate.name)),
                        Color.Black
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pokemonSpecies.egg_groups.forEach {
                            TextInfo(text = parseEggGroups(eggGroup = it.name), Color.Black)
                        }
                    }
                }
            }
        }

        if (isDialogShown) {
            AbilityDialog(
                dominantColor = dominantColor,
                onDismiss = { isDialogShown = false },
                ability = ability!!
            )
        }
        Spacer(modifier = Modifier.height(70.dp))
    }
}

@Composable
fun AbilityDialog(
    dominantColor: Color,
    onDismiss: () -> Unit,
    ability: AbilityDescription
) {
    val noFlavor = stringResource(id = R.string.no_flavor)
    var flavorEntry by remember {
        mutableStateOf(noFlavor)
    }
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(110.dp)
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .background(Color.White, RoundedCornerShape(10.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(dominantColor.copy(0.5f), RoundedCornerShape(10.dp))
            ) {
                ability.flavor_text_entries.forEach {
                    if (it.language.name == Locale.getDefault().language) {
                        flavorEntry = it.flavor_text
                    }
                }
                Row (modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(
                        text = flavorEntry,
                        color = Color.Black,
                        fontFamily = fontBasic(),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}