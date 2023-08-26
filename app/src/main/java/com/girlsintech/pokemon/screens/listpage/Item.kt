package com.girlsintech.pokemon.screens.listpage

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.screens.fontPokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.util.SelectedPokemon
import com.girlsintech.pokemon.util.parseType
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*

//item di un Pokémon all'interno della lista, contenente il nome, l'immagine, il tipo e il bottone dei preferiti
@Composable
fun PokemonItem(
    pokemon: Pokemon,
    refresh: Boolean,
    viewModel: PokemonViewModel,
    navController: NavController,
    onRefresh: (Boolean) -> Unit
) {
    var dominantColor by remember {
        mutableStateOf(BluePokemon.copy(0.4f))
    }

    Column {
        Box(
            modifier = Modifier
                .padding(top = 5.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(dominantColor.copy(alpha = 0.6f))
                .clickable {
                    //cliccando sull'item si apre la schermata di dettaglio
                    SelectedPokemon.selectPokemon(dominantColor, pokemon)
                    navController.navigate("pokemon_detail_screen")
                }
                .fillMaxWidth()
        ) {
            val idImage = if (pokemon.id > 1010) {
                pokemon.id + 8990
            } else {
                pokemon.id
            }

            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$idImage.png")
                    .diskCacheKey("pokemon_color_${pokemon.id}")
                    .listener { _, result ->
                        viewModel.calcDominantColor(result.drawable) { color ->
                            dominantColor = color
                        }
                    }
                    .build(),
                contentDescription = null,
                modifier = Modifier.alpha(0f)
            )

            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (description, image, icon) = createRefs()

                Column(
                    modifier = Modifier
                        .constrainAs(description) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 10.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .padding(8.dp)
                ) {
                    Text(
                        text = pokemon.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        fontFamily = fontPokemon(),
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(5.dp)
                    ) {
                        val types = pokemon.type
                        val delim = ", "

                        types.split(delim).forEach {
                            Text(
                                text = parseType(type = it),
                                fontFamily = fontPokemon(),
                                fontSize = 15.sp,
                                color = Color.White
                            )
                        }
                    }
                }

                SubcomposeAsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(pokemon.img)
                        .diskCacheKey("pokemon_image_${pokemon.id}")
                        .build(),
                    contentDescription = null,
                    loading = {
                        CircularProgressIndicator(
                            modifier = Modifier.requiredSize(30.dp),
                            color = BluePokemon,
                            strokeWidth = 2.dp
                        )
                    },

                    modifier = Modifier
                        .constrainAs(image)
                        {
                            top.linkTo(parent.top)
                            end.linkTo(icon.start, 15.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(90.dp)
                )

                Icon(
                    Icons.TwoTone.Favorite,
                    modifier = Modifier
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end, 20.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .clickable {
                            pokemon.favorite = 1 - pokemon.favorite
                            viewModel.update(pokemon)
                            onRefresh(refresh)
                        }
                        .size(30.dp),
                    contentDescription = null,
                    tint = if (pokemon.favorite == 1) Color.Red else Color.White,
                )
            }

        }
    }
}
