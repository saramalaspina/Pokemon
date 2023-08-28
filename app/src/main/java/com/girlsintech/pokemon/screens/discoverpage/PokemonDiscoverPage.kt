package com.girlsintech.pokemon.screens.discoverpage

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.screens.fontPokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.Discover
import com.girlsintech.pokemon.util.LoadingGift
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*
import kotlin.concurrent.schedule

//pagina in cui vengono mostrate le principali caratteristiche di un Pokèmon estratto casualmente
@Composable
fun PokemonDiscoverPage(
    navController: NavController,
    viewModel: PokemonViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Discover.copy(0.3f)
    ) {

        val configuration = LocalConfiguration.current

        val sfondo = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
            R.drawable.img
        } else {
            R.drawable.sfondo_discover_h
        }

        Row {
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = sfondo),
                contentDescription = "background",
                contentScale = ContentScale.FillBounds,
                alpha = 0.8f
            )
        }

        var visibility by remember {
            mutableStateOf(false)
        }

        var discover by rememberSaveable {
            mutableStateOf(MyState.Load)
        }

        var pokemon: Pokemon? by rememberSaveable {
            mutableStateOf(null)
        }

        if(pokemon == null) {
            pokemon = viewModel.randomPokemon
        }

        if (pokemon != null) {
            //timer utilizzato per permettere il completamento dell'animazione di caricamento
            Timer().schedule(3800) {
                discover = MyState.Success
            }
        }


        when (discover) {
            MyState.Success -> {
                Row {
                    Icon(
                        Icons.TwoTone.ArrowBack,
                        contentDescription = null,
                        tint = BluePokemon,
                        modifier = Modifier
                            .padding(top = 20.dp, start = 20.dp)
                            .size(33.dp)
                            .clickable {
                                navController.navigate("pokemon_homepage")
                            }
                    )
                }

                //timer utilizzato per rendere visibile l'animazione che mostra il Pokémon
                Timer().schedule(100) {
                    visibility = true
                }

                //la pagina viene composta diversamente a seconda dell'orientamento
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        AnimatedVisibility(
                            visible = visibility,
                            enter = expandVertically(),
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    top = 80.dp,
                                    start = 30.dp,
                                    end = 30.dp
                                )
                            ) {
                                Text(
                                    text = stringResource(id = R.string.discover_text),
                                    textAlign = TextAlign.Center,
                                    fontFamily = fontPokemon(),
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 330.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Top
                            ) {
                                DiscoveredPokemon(pokemon!!, viewModel, 350.dp, 100.dp, 20.dp)
                            }
                            DiscoveredImage(
                                url = pokemon!!.img,
                                modifier = Modifier
                                    .size(250.dp)
                                    .offset(y = 180.dp))
                        }
                    }
                    else -> {
                        AnimatedVisibility(
                            visible = visibility,
                            enter = expandHorizontally(),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.Top
                            ) {
                                Text(
                                    text = stringResource(id = R.string.discover_text),
                                    fontFamily = fontPokemon(),
                                    fontSize = 20.sp,
                                    color = Color.White
                                )
                            }

                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(top = 70.dp, start = 132.dp, end = 60.dp),
                                verticalArrangement = Arrangement.Top
                            ) {
                                DiscoveredPokemon(pokemon!!, viewModel, 550.dp, 20.dp, 20.dp)
                            }
                            DiscoveredImage(
                                url = pokemon!!.img,
                                modifier = Modifier
                                    .size(250.dp)
                                    .offset(y = 130.dp, x = 150.dp)
                            )
                        }
                    }
                }

            }

            MyState.Error -> {}

            MyState.Load, MyState.Init -> {
                LoadingGift()
            }
        }
    }
}





