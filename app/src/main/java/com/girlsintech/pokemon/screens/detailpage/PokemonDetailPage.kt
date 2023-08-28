package com.girlsintech.pokemon.screens.detailpage

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.girlsintech.pokemon.data.remote.ability.AbilityDescription
import com.girlsintech.pokemon.data.remote.evolution.Evolution
import com.girlsintech.pokemon.data.remote.species.Species
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.util.ErrorMessage
import com.girlsintech.pokemon.util.Loading
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*
import kotlin.concurrent.schedule

//pagina di dettaglio del Pokémon
@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun PokemonDetailPage(
    dominantColor: Color,
    pokemon: Pokemon,
    viewModel: PokemonDetailViewModel,
    viewModelDb: PokemonViewModel,
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = dominantColor.copy(0.6f)
    ) {

        var pokemonSpecies: Species? by remember {
            mutableStateOf(null)
        }

        var evolutionChain: Evolution? by remember {
            mutableStateOf(null)
        }

        var ability1: AbilityDescription? by remember {
            mutableStateOf(null)
        }

        var ability2: AbilityDescription? by remember {
            mutableStateOf(null)
        }

        var ability3: AbilityDescription? by remember {
            mutableStateOf(null)
        }

        var refresh by remember {
            mutableStateOf(MyState.Load)
        }

        var refreshEvolution by remember {
            mutableStateOf(MyState.Load)
        }

        var navState by remember {
            mutableStateOf(0)
        }

        var message by remember {
            mutableStateOf("")
        }

        val pokemonInfo = viewModel.pokemonInfo.observeAsState().value

        val numAbility = pokemonInfo!!.abilities.size


        //richiesta della prima abilità nelle PokeAPI
        viewModel.getAbility(
            pokemonInfo.abilities[0].ability.url,
            {
                refresh = MyState.Error
                message = it
            },
            {
                ability1 = it
            }
        )

        //richiesta della seconda abilità nelle PokeAPI
        if (numAbility > 1) {
            viewModel.getAbility(
                pokemonInfo.abilities[1].ability.url,
                {
                    refresh = MyState.Error
                    message = it
                },
                {
                    ability2 = it
                }
            )
        }

        //richiesta della terza abilità nelle PokeAPI
        if (numAbility > 2) {
            viewModel.getAbility(
                pokemonInfo.abilities[2].ability.url,
                {
                    refresh = MyState.Error
                    message = it
                },
                {
                    ability3 = it
                }
            )
        }

        //richiesta della specie nelle PokeAPI
        viewModel.getSpecies(
            pokemonInfo.species.url,
            {
                refreshEvolution = MyState.Error
                message = it
            },
            {
                pokemonSpecies = it
                refreshEvolution = MyState.Success
            }
        )

        when (refreshEvolution) {
            MyState.Success -> {
                //dalle informazioni riguardanti la specie ottengo l'url per effettuare la richiesta delle evoluzioni nelle PokeAPI
                viewModel.getEvolution(pokemonSpecies!!.evolution_chain.url,
                    {
                        refresh = MyState.Error
                        message = it
                    },
                    {
                        evolutionChain = it
                        //timer utilizzato per mostrate l'animazione di caricamento
                        Timer().schedule(1500) {
                            if (refresh != MyState.Error) {
                                //se non ci sono stati errori nelle richieste effettuate per le precedenti informazioni la richiesta può andare a buon fine
                                refresh = MyState.Success
                            }
                        }
                    }
                )
            }

            MyState.Error -> {
                ErrorMessage(message = message)
            }

            MyState.Load, MyState.Init -> {}
        }

        val configuration = LocalConfiguration.current

        when (refresh) {
            MyState.Success -> {

                when (configuration.orientation) {
                    //la pagina viene composta diversamente a seconda dell'orientamento
                    Configuration.ORIENTATION_PORTRAIT -> {

                        ConstraintLayout(modifier = Modifier.fillMaxSize()) {

                            val (first, image, detail) = createRefs()

                            Column(modifier = Modifier
                                .constrainAs(first) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                })
                            {
                                TopIcons(
                                    pokemon = pokemon,
                                    viewModel = viewModelDb,
                                    navController = navController
                                )

                                TopBox(
                                    pokemonInfo = pokemonInfo,
                                    dominantColor
                                )
                            }

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .offset(y = 140.dp)
                                    .background(Color.White, RoundedCornerShape(10))
                                    .constrainAs(detail) {
                                        top.linkTo(first.bottom)
                                        start.linkTo(parent.start)
                                    }
                            ) {
                                Spacer(modifier = Modifier.height(110.dp))

                                NavigationBar {
                                    navState = it
                                }

                                Spacer(modifier = Modifier.height(25.dp))

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    //a seconda della pagina richiesta nella navigation bar vengono mostrate informazioni diverse
                                    when (navState) {
                                        0 -> PokemonDetailSection(
                                            dominantColor = dominantColor,
                                            pokemonInfo = pokemonInfo,
                                            pokemonSpecies = pokemonSpecies!!,
                                            ability1 = ability1,
                                            ability2 = ability2,
                                            ability3 = ability3,
                                            330.dp
                                        )
                                        1 -> PokemonStatSection(pokemonInfo = pokemonInfo, 330.dp)
                                        2 -> PokemonEvolutionSection(
                                            viewModelDb = viewModelDb,
                                            evolution = evolutionChain!!,
                                            300.dp
                                        )
                                    }
                                }
                            }
                            Column(modifier = Modifier
                                .constrainAs(image) {
                                    top.linkTo(first.bottom)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                }) {
                                ImageBox(
                                    pokemon.img,
                                    modifier = Modifier
                                        .size(250.dp),
                                )
                            }
                        }
                    }
                    else -> {
                        ConstraintLayout {
                            val (icons, image, name, detail) = createRefs()

                            Column(modifier = Modifier
                                .constrainAs(icons) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start, 5.dp)
                                }) {
                                TopIcons(
                                    pokemon = pokemon,
                                    viewModel = viewModelDb,
                                    navController = navController
                                )
                            }

                            Column(modifier = Modifier
                                .constrainAs(name) {
                                    top.linkTo(parent.top, 12.dp)
                                    start.linkTo(parent.start, 80.dp)
                                }) {
                                TopBox(
                                    pokemonInfo = pokemonInfo,
                                    dominantColor,
                                )
                            }

                            Column(modifier = Modifier
                                .constrainAs(image) {
                                    top.linkTo(name.bottom)
                                    start.linkTo(parent.start, 20.dp)
                                    bottom.linkTo(parent.bottom)
                                }) {
                                ImageBox(
                                    pokemon.img,
                                    modifier = Modifier
                                        .size(250.dp)
                                        .offset(x = 40.dp),
                                )
                            }


                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .width(420.dp)
                                    .height(360.dp)
                                    .background(Color.White, RoundedCornerShape(10))
                                    .constrainAs(detail) {
                                        top.linkTo(parent.top, 55.dp)
                                        start.linkTo(parent.start, 345.dp)
                                    }
                            ) {

                                Spacer(modifier = Modifier.height(25.dp))

                                NavigationBar {
                                    navState = it
                                }

                                Spacer(modifier = Modifier.height(25.dp))

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    //a seconda della pagina richiesta nella navigation bar vengono mostrate informazioni diverse
                                    when (navState) {
                                        0 -> PokemonDetailSection(
                                            dominantColor = dominantColor,
                                            pokemonInfo = pokemonInfo,
                                            pokemonSpecies = pokemonSpecies!!,
                                            ability1 = ability1,
                                            ability2 = ability2,
                                            ability3 = ability3,
                                            20.dp
                                        )
                                        1 -> PokemonStatSection(pokemonInfo = pokemonInfo, 30.dp)
                                        2 -> PokemonEvolutionSection(
                                            viewModelDb = viewModelDb,
                                            evolution = evolutionChain!!,
                                            20.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            MyState.Error -> {
                ErrorMessage(message)
            }
            MyState.Load, MyState.Init -> {
                Loading()
            }
        }
    }
}



