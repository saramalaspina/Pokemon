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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.data.remote.responses.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.util.ScreenRouter
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel
import java.lang.Math.round
import java.util.*
import kotlin.math.roundToInt

@Composable
fun PokemonDetailPage (
    dominantColor: Color,
    imgUrl: String,
    viewModel: PokemonDetailViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = dominantColor.copy(0.6f)
    ) {

        var pokemon = viewModel.pokemonInfo.observeAsState().value
        TopBox(pokemon = pokemon!!, dominantColor)
        PokemonDetailSection(pokemon = pokemon!!)
        ImageBox(imgUrl)
    }
}

@Composable
fun TopBox(
    pokemon: Pokemon,
    dominantColor: Color
) {
    Column {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
        ) {
            ConstraintLayout {
                val (arrow, favorite) = createRefs()

                Icon(
                    Icons.TwoTone.ArrowBack,
                    contentDescription = null,
                    tint = BluePokemon,
                    modifier = Modifier
                        .constrainAs(arrow) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 15.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .requiredSize(30.dp)
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
                        .requiredSize(30.dp)
                        .clickable {
                            ScreenRouter.navigateTo(3, 2)
                        }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box {

            Column (modifier = Modifier.padding(start = 25.dp, top = 5.dp)){
                Text(
                    text = "N° ${pokemon.id}",
                    fontFamily = fontPokemon(),
                    fontSize = 25.sp,
                    color = Color.White
                )

                Text(
                    text = pokemon.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    },
                    fontFamily = fontPokemon(),
                    fontSize = 30.sp,
                    color = Color.White
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pokemon.types.forEach { s ->
                        Text(
                            text = s.type.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.getDefault()
                                ) else it.toString()
                            },
                            fontFamily = fontPokemon(),
                            fontSize = 20.sp,
                            color = Color.White,
                            modifier = Modifier
                                .background(dominantColor.copy(0.8f), RoundedCornerShape(50))
                                .padding(horizontal = 8.dp)
                        )

                    }
                }
            }
        }
    }
}

@Composable
fun ImageBox(
    imgUrl: String
) {
    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .fillMaxSize()
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .offset(y = 180.dp)
        )
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
            .offset(y = 330.dp)
            .verticalScroll(scrollState)
            .background(Color.White, RoundedCornerShape(10))
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 25.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextInfo(text = "Species")
                TextInfo(text = "Height")
                TextInfo(text = "Weight")
                TextInfo(text = "Abilities")
            }

            Spacer(modifier = Modifier.width(60.dp))

            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextInfo(text = pokemon.species.name, Color.Black)
                TextInfo(text ="${(pokemon.height * 100f).roundToInt() / 1000f} m", Color.Black)
                TextInfo(text ="${(pokemon.weight * 100f).roundToInt() / 1000f} kg", Color.Black)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ){
                    pokemon.abilities.forEach{
                        TextInfo(text = it.ability.name, Color.Black)
                    }
                }
            }
        }
    }
}

@Composable
fun TextInfo(
    text : String,
    color: Color = Color.Gray
){
    Text(
        text = text.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
        fontSize = 15.sp,
        fontFamily = fontBasic(),
        color = color
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
