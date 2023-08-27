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
import androidx.compose.ui.unit.Dp
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

//component che mostra le informazioni generali di un Pokèmon, tra cui
// specie, altezza, peso, abilità, generazione, tasso di crescita e gruppo uova
@Composable
fun PokemonDetailSection(
    dominantColor: Color,
    pokemonInfo: PokemonInfo,
    pokemonSpecies: Species,
    ability1: AbilityDescription?,
    ability2: AbilityDescription?,
    ability3: AbilityDescription?,
    bottomPadding: Dp
) {
    val noAvailable = stringResource(id = R.string.no_available)

    var species by remember {
        mutableStateOf(noAvailable)
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
            .padding(start = 25.dp, bottom = bottomPadding)
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
                    val language = if(Locale.getDefault().language == "it"){
                        "it"
                    } else {
                        "en"
                    }

                    pokemonSpecies.genera.forEach {
                        //in base alla localizzazione assegno la specie con la traduzione corretta
                        if (it.language.name == language) {
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
                        //non controllo se l'abilità esiste in quanto tutti i Pokèmon hanno almeno un'abilità
                        SetAbility(ability = ability1!!) {
                            isDialogShown = true
                            ability = ability1
                            abilityDescription = ability1.name
                        }

                        //controllo l'esistenza della seconda abilità poichè è facoltativa
                        if(ability2 != null){
                            SetAbility(ability = ability2) {
                                isDialogShown = true
                                ability = ability2
                                abilityDescription = ability2.name
                            }
                        }

                        //controllo l'esistenza della terza abilità poichè è facoltativa
                        if(ability3 != null){
                            SetAbility(ability = ability3) {
                                isDialogShown = true
                                ability = ability3
                                abilityDescription = ability3.name
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
                        if (pokemonSpecies.egg_groups.isEmpty()){
                            TextInfo(text = noAvailable, Color.Black)
                        }

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
        Spacer(modifier = Modifier.height(10.dp))
    }
}

//component usata per mostrare l'abilità nella lingua richiesta
@Composable
fun SetAbility(
    ability: AbilityDescription,
    onClick: () -> Unit
){
    val language = if(Locale.getDefault().language == "it"){
        "it"
    } else {
        "en"
    }

    ability.names.forEach { name ->
        if (name.language.name == language) {
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
                        onClick()
                    }
            )
        }
    }
}

//dialog che mostra il flavor text di un'abilità quando questa viene cliccata
@Composable
fun AbilityDialog(
    dominantColor: Color,
    onDismiss: () -> Unit,
    ability: AbilityDescription
) {
    val language = if(Locale.getDefault().language == "it"){
        "it"
    } else {
        "en"
    }

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
                //la descrizione viene mostrata nella lingua di default di sistema
                ability.flavor_text_entries.forEach {
                    if (it.language.name == language) {
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