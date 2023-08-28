package com.girlsintech.pokemon.screens.detailpage

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.screens.fontPokemon
import com.girlsintech.pokemon.util.parseType
import java.util.*

//component che mostra il nome, il numero e il tipo del Pokémon
@Composable
fun TopBox(
    pokemonInfo: PokemonInfo,
    dominantColor: Color,
) {
    val configuration = LocalConfiguration.current

    when (configuration.orientation) {
        Configuration.ORIENTATION_PORTRAIT -> {
            Column {

                Spacer(modifier = Modifier.height(10.dp))

                Box {

                    Column(modifier = Modifier.padding(start = 25.dp, top = 5.dp, end = 25.dp)) {
                        Text(
                            text = "N° ${pokemonInfo.id}",
                            fontFamily = fontPokemon(),
                            fontSize = 20.sp,
                            color = Color.White
                        )

                        Text(
                            text = pokemonInfo.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            fontFamily = fontPokemon(),
                            fontSize = 20.sp,
                            color = Color.White
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pokemonInfo.types.forEach {
                                Text(
                                    text = parseType(type = it.type.name),
                                    fontFamily = fontPokemon(),
                                    fontSize = 16.sp,
                                    color = Color.White,
                                    modifier = Modifier
                                        .background(
                                            dominantColor.copy(0.8f),
                                            RoundedCornerShape(50)
                                        )
                                        .padding(horizontal = 8.dp)
                                )

                            }
                        }
                    }
                }
            }
        }
        else -> {
            Column {
                Spacer(modifier = Modifier.height(2.dp))
                Row {
                    Text(
                        text = "N° ${pokemonInfo.id}",
                        fontFamily = fontPokemon(),
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier .width(20.dp))

                    Text(
                        text = pokemonInfo.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        fontFamily = fontPokemon(),
                        fontSize = 20.sp,
                        color = Color.White
                    )

                }
            }

            Spacer(modifier = Modifier .height(15.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                pokemonInfo.types.forEach {
                    Text(
                        text = parseType(type = it.type.name),
                        fontFamily = fontPokemon(),
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .background(
                                dominantColor.copy(0.8f),
                                RoundedCornerShape(50)
                            )
                            .padding(horizontal = 8.dp)
                    )

                }
            }
        }
    }
}

//box che contiene l'immagine del Pokémon
@Composable
fun ImageBox(
    imgUrl: String,
    modifier: Modifier,
) {
    Box {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .build(),
            contentDescription = null,
            modifier = modifier
        )
    }
}