package com.girlsintech.pokemon.screens.discoverpage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.model.SingletonListOfAbilities
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.screens.fontPokemon
import com.girlsintech.pokemon.ui.theme.Discover
import com.girlsintech.pokemon.util.TextInfo
import com.girlsintech.pokemon.util.parseGenerationFromInt
import com.girlsintech.pokemon.util.parseType
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*

//component che mostra una card con all'interno le caratteristiche principali del Pokémon scoperto, tra cui
//immagine, nome, tipo, abilità e generazione
@Composable
fun DiscoveredPokemon(
    pokemon: Pokemon,
    viewModel: PokemonViewModel,
    widthBox: Dp,
    namePadding: Dp,
    titlePadding: Dp
) {

    var fav by remember {
        mutableStateOf(pokemon.favorite)
    }
    var boxSize by remember {
        mutableStateOf(Size.Zero)
    }

    val scrollState = rememberScrollState()

    ConstraintLayout {

        val (card1, card2) = createRefs()

        Card(
            elevation = 15.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .constrainAs(card2) {
                    start.linkTo(card1.start, 6.dp)
                    top.linkTo(card1.top, 10.dp)
                }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .width(with(LocalDensity.current) { boxSize.width.toDp() })
                    .height(with(LocalDensity.current) { boxSize.height.toDp() })
            )
        }

        Card(
            elevation = 5.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .constrainAs(card1) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .onGloballyPositioned { coordinates ->
                    boxSize = coordinates.size.toSize()
                }
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Discover.copy(1f),
                                Discover.copy(0.9f),
                                Discover.copy(0.7f)
                            )
                        ), RoundedCornerShape(20.dp)
                    )
                    .width(widthBox)
            ) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp)
                ) {
                    val (name, favorite, detail) = createRefs()

                    Text(
                        text = pokemon.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        modifier = Modifier
                            .constrainAs(name) {
                                top.linkTo(parent.top, namePadding)
                                start.linkTo(parent.start)
                            },
                        color = Color.White,
                        fontSize = 20.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = fontPokemon()
                    )

                    Icon(
                        Icons.TwoTone.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .constrainAs(favorite) {
                                top.linkTo(parent.top, 20.dp)
                                end.linkTo(parent.end)
                            }
                            .requiredSize(33.dp)
                            .clickable {
                                pokemon.favorite = 1 - pokemon.favorite
                                fav = 1 - fav
                                viewModel.update(pokemon)
                            }
                            .size(30.dp),
                        tint = if (fav == 1) Color.Red else Color.White
                    )


                    Column(
                        modifier = Modifier
                            .padding(top = titlePadding, bottom = 30.dp)
                            .constrainAs(detail) {
                                top.linkTo(name.bottom)
                                start.linkTo(name.start)

                            },
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {

                        Row {
                            ConstraintLayout {
                                val (col1, col2) = createRefs()
                                Column(
                                    modifier = Modifier
                                        .constrainAs(col1) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                        }
                                ) {
                                    TextInfo(
                                        text = stringResource(id = R.string.selection_type),
                                        Color.White
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier
                                        .constrainAs(col2) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start, 130.dp)
                                        }
                                ) {
                                    val delimType = ", "
                                    pokemon.type.split(delimType).forEach {
                                        TextInfo(text = parseType(type = it), color = Color.White)
                                    }
                                }
                            }
                        }

                        Row {
                            ConstraintLayout {
                                val (col1, col2) = createRefs()

                                Column(
                                    modifier = Modifier
                                        .constrainAs(col1) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                        }
                                ) {
                                    TextInfo(
                                        text = stringResource(id = R.string.search_ability),
                                        Color.White
                                    )
                                }
                                Column(
                                    verticalArrangement = Arrangement.spacedBy(5.dp),
                                    modifier = Modifier
                                        .constrainAs(col2) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start, 130.dp)
                                        }
                                ) {
                                    val delimAbility = ","
                                    pokemon.ability.split(delimAbility).forEach {
                                        if (it.isNotBlank()) {
                                            if(Locale.getDefault().language == "it") {
                                                GetAbility(ability = it)
                                            } else {
                                                TextInfo(text = it, color = Color.White)
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        Row {
                            ConstraintLayout {
                                val (col1, col2) = createRefs()

                                Column(
                                    modifier = Modifier
                                        .constrainAs(col1) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start)
                                        }
                                ) {
                                    TextInfo(
                                        text = stringResource(id = R.string.generation),
                                        Color.White
                                    )
                                }
                                Column(
                                    modifier = Modifier
                                        .constrainAs(col2) {
                                            top.linkTo(parent.top)
                                            start.linkTo(parent.start, 130.dp)
                                        }
                                ) {
                                    TextInfo(
                                        text = parseGenerationFromInt(pokemon.generation),
                                        Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun GetAbility(
    ability: String
) {
    var abilityIt by remember {
        mutableStateOf("")
    }

    //se la lingua è impostata in italiano è necessario ricavare dalla lista delle abilità la corretta traduzione
    val listOfAbilities = SingletonListOfAbilities.getInstance(LocalContext.current)
    listOfAbilities.abilities.forEach {
        if (ability == it.en){
            abilityIt = it.it
        }
    }
    if(abilityIt.isNotBlank()) {
        TextInfo(text = abilityIt, color = Color.White)
    } else {
        TextInfo(text = ability, color = Color.White)
    }
}

//box contenente l'immagine dei Pokémon
@Composable
fun DiscoveredImage(
    url: String,
    modifier: Modifier
){

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .build(),
            contentDescription = null,
            modifier = modifier
        )
    }
}