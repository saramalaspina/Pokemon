package com.girlsintech.pokemon.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.data.remote.responses.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.util.ScreenRouter
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
        topBox(pokemon = pokemon!!, dominantColor)
        imageBox(pokemon = pokemon!!)
    }
}

@Composable
fun topBox(
    pokemon : Pokemon,
    dominantColor: Color
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

                    Row (
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ){
                        pokemon.types.forEach { s ->
                            Text(
                                text = s.type.name.replaceFirstChar {
                                    if (it.isLowerCase()) it.titlecase(
                                        Locale.getDefault()
                                    ) else it.toString()
                                },
                                fontFamily = fontPokemon(),
                                fontSize = 25.sp,
                                color = Color.White,
                                modifier = Modifier
                                    .background(dominantColor.copy(0.3f), RoundedCornerShape(50))
                                    .padding(horizontal = 8.dp)
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
fun imageBox(
    pokemon: Pokemon
){
    Box(contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()) {
            pokemon.sprites?.let {
                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(it.front_default)
                        .diskCacheKey("pokemon_image_${pokemon.id}")
                        .build(),
                    contentDescription = null,
                    modifier = Modifier
                        .size(280.dp)
                        .offset(y = 180.dp)
                )
            }
    }
}

@Composable
fun PokemonDetailSection(
    pokemon: Pokemon
){
    val scrollState = rememberScrollState()
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Row() {
            Column() {
                
            }
            TextInfo(text = "Species")
            TextInfo(text = "")
        }
    }
}

@Composable
fun TextInfo(
    text : String
){
    Text(
        text = text,
        fontSize = 15.sp,
        fontFamily = fontBasic(),
        color = Color.LightGray
    ) 
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
