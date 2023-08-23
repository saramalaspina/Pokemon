package com.girlsintech.pokemon.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.data.remote.species.Species
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.Yellow
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.roundToInt

@Composable
fun PokemonDiscoverPage(
    navController: NavController,
    viewModel: PokemonDetailViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Yellow.copy(0.4f)
    ) {

        var discover by remember {
            mutableStateOf(MyState.Load)
        }

        var pokemonSpecies: Species? by remember {
            mutableStateOf(null)
        }

        Timer().schedule(4500) {
            discover = MyState.Success
        }

        val pokemonInfo = viewModel.pokemonInfo.observeAsState().value

        when (discover) {
            MyState.Success -> {
                Icon(
                    Icons.TwoTone.ArrowBack,
                    contentDescription = null,
                    tint = BluePokemon,
                    modifier = Modifier
                        .requiredSize(33.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
            }

            MyState.Error -> {}

            MyState.Load, MyState.Init -> {
                Loading()
            }
        }
    }
}

@Composable
fun DiscoverPokemon(
    pokemonInfo: PokemonInfo,
    pokemonSpecies: Species
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
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
                    modifier = Modifier.constrainAs(detailCol1) {
                        start.linkTo(parent.start)
                        top.linkTo(parent.top)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    TextInfo(text = stringResource(id = com.girlsintech.pokemon.R.string.species))
                    TextInfo(text = stringResource(id = com.girlsintech.pokemon.R.string.height))
                    TextInfo(text = stringResource(id = com.girlsintech.pokemon.R.string.weight))
                }

                Column(
                    modifier = Modifier.constrainAs(detailCol2) {
                        start.linkTo(parent.start, 130.dp)
                        top.linkTo(parent.top)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pokemonSpecies.genera.forEach {
                        if (it.language.name == Locale.getDefault().language) {
                            TextInfo(text = it.genus, Color.Black)
                        }
                    }

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
                        pokemonInfo.abilities.forEach { ability ->
                            Text(
                                text = ability.ability.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.ROOT
                                    ) else it.toString()
                                },
                                color = Color.Black,
                                fontSize = 15.sp,
                                fontFamily = fontBasic()
                            )
                        }
                    }
                }
            }
        }
    }
}

