package com.girlsintech.pokemon.screens

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.Discover
import com.girlsintech.pokemon.ui.theme.Yellow
import com.girlsintech.pokemon.util.parseGenerationFromInt
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*
import kotlin.concurrent.schedule


@Composable
fun PokemonDiscoverPage(
    navController: NavController,
    viewModel: PokemonViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Discover.copy(0.3f)
    ) {

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

                Timer().schedule(600) {
                    visibility = true
                }

                AnimatedVisibility(
                    visible = visibility,
                    enter =  expandVertically(),
                ) {
                    DiscoveredPokemon(pokemon!!, viewModel)
                }
            }

            MyState.Error -> {}

            MyState.Load, MyState.Init -> {
                LoadingGift()
            }
        }
    }
}

@Composable
fun DiscoveredPokemon(
    pokemon: Pokemon,
    viewModel: PokemonViewModel
) {
    val configuration = LocalConfiguration.current

    val withBox = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
        350.dp
    } else {
        500.dp
    }

    var fav by remember {
        mutableStateOf(pokemon.favorite)
    }

    var scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 250.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            elevation = 15.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .background(BluePokemon.copy(0.8f), RoundedCornerShape(20.dp))
                    .width(withBox)
            ) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    val (name, favorite) = createRefs()

                    Text(
                        text = pokemon.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .padding(8.dp)
                            .constrainAs(name) {
                                top.linkTo(parent.top, 90.dp)
                                start.linkTo(parent.start, 20.dp)
                            },
                        color = Color.White,
                        fontSize = 25.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = fontPokemon()
                    )

                    Icon(
                        Icons.TwoTone.Favorite,
                        contentDescription = null,
                        modifier = Modifier
                            .constrainAs(favorite) {
                                top.linkTo(parent.top, 20.dp)
                                end.linkTo(parent.end, 20.dp)
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
                }


                Column(
                    modifier = Modifier
                        .padding(top = 170.dp, start = 20.dp, bottom = 50.dp),
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
                                        start.linkTo(parent.start, 20.dp)
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
                                    TextInfo(text = it, color = Color.White)
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
                                        start.linkTo(parent.start, 20.dp)
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
                                        TextInfo(text = it, color = Color.White)
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
                                        start.linkTo(parent.start, 20.dp)
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
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        }
    }
    DiscoveredImage(url = pokemon.img)
}

@Composable
fun DiscoveredImage(url: String){
    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .size(250.dp)
                .offset(y = 90.dp)
        )
    }
}

@Composable
fun LoadingGift() {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.gift_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever,
        speed = 0.5f
    )
    ConstraintLayout {
        val (pi) = createRefs()
        LottieAnimation(modifier = Modifier
            .size(300.dp)
            .constrainAs(pi) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            composition = composition, progress = {progress}
        )
    }
}



