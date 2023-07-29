package com.girlsintech.pokemon.screens

import android.app.Application
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.girlsintech.pokemon.data.remote.responses.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.util.ScreenRouter
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel
import java.util.*

@Composable
fun PokemonDetailPage (
    dominantColor: Color,
    viewModel: PokemonDetailViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = dominantColor.copy(0.5f)
    ) {

        var pokemon = viewModel.pokemonInfo.observeAsState().value
        topBox(pokemon = pokemon!!)
    }
}

@Composable
fun topBox(
    pokemon : Pokemon
){
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 18.dp)
        ) {
            ConstraintLayout {
                val (arrow, favorite) = createRefs()

                Icon(
                    Icons.TwoTone.ArrowBack,
                    contentDescription = null,
                    tint = BluePokemon,
                    modifier = Modifier
                        .constrainAs(arrow) {
                            top.linkTo(parent.top, 20.dp)
                            start.linkTo(parent.start)
                            bottom.linkTo(parent.bottom)
                        }
                        .requiredSize(40.dp)
                        .clickable {
                            ScreenRouter.navigateTo(3, 2)
                        }
                )
                Icon(
                    Icons.TwoTone.Favorite,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier
                        .constrainAs(favorite) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 340.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .requiredSize(40.dp)
                        .clickable {
                            ScreenRouter.navigateTo(3, 2)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box {
            ConstraintLayout {
                val (description, number) = createRefs()

                Column(
                    modifier = Modifier
                        .constrainAs(description) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 20.dp)
                            bottom.linkTo(parent.bottom)
                        }
                ) {
                    Text(
                        text = pokemon!!.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        fontFamily = fontPokemon(),
                        fontSize = 35.sp,
                        color = Color.White
                    )

                    Row {
                        pokemon.types.forEach { s ->
                            Text(
                                text = s.type.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                } + " ",
                                fontFamily = fontPokemon(),
                                fontSize = 25.sp,
                                color = Color.White
                            )
                        }
                    }
                }
                Text(
                    modifier = Modifier
                        .constrainAs(number) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 300.dp)
                            bottom.linkTo(parent.bottom)
                        },
                    text = "N° ${pokemon!!.id}",
                    fontFamily = fontPokemon(),
                    fontSize = 28.sp,
                    color = Color.White
                )
            }
        }
    }
}


@Composable
fun ErrorMessage(message: String) {
    ConstraintLayout {
        val msg = createRef()

        Text(
            text = message, modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                .padding(8.dp)
                .constrainAs(msg) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                },
            fontSize = 16.sp
        )
    }
}

@Composable
fun Loading() {
    ConstraintLayout {
        val (pi, wp) = createRefs()
        CircularProgressIndicator(modifier = Modifier.constrainAs(pi) {
            top.linkTo(parent.top)
            start.linkTo(parent.start)
            bottom.linkTo(parent.bottom)
            end.linkTo(parent.end)
        })
        Text(text = "Wait, please", modifier = Modifier
            .constrainAs(wp) {
                top.linkTo(pi.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}
