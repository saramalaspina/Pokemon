package com.girlsintech.pokemon.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
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
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.*
import com.girlsintech.pokemon.util.parseGenerationFromInt
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.concurrent.schedule


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun PokemonDiscoverPage(
    navController: NavController,
    viewModel: PokemonViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Discover.copy(0.5f)
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
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            elevation = 15.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
        Box(
            modifier = Modifier
                .verticalScroll(scrollState)
                .background(CardBackground, RoundedCornerShape(20.dp))
                .width(withBox)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val (name, favorite) = createRefs()

                Text(
                    text = pokemon.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() },
                    modifier = Modifier
                        .padding(4.dp)
                        .padding(8.dp)
                        .constrainAs(name) {
                            top.linkTo(parent.top, 43.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    color = BluePokemon,
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
                    tint = if (fav == 1) Color.Red else Color.LightGray
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 110.dp)
            ) {
                DiscoveredImage(url = pokemon.img)
            }

            Column(
                modifier = Modifier
                    .padding(top = 350.dp, start = 20.dp, bottom = 50.dp),
                horizontalAlignment = Alignment.Start
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
                            TextInfo(text = stringResource(id = R.string.selection_type))
                        }
                        Column(
                            modifier = Modifier
                                .constrainAs(col2) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start, 130.dp)
                                }
                        ) {
                            val delimType = ", "
                            pokemon.type.split(delimType).forEach {
                                TextInfo(text = "$it\n", color = Color.Black)
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
                            TextInfo(text = stringResource(id = R.string.search_ability))
                        }
                        Column(
                            modifier = Modifier
                                .constrainAs(col2) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start, 130.dp)
                                }
                        ) {
                            val delimAbility = ","
                            pokemon.ability.split(delimAbility).forEach {
                                if (it.isNotBlank()) {
                                    TextInfo(text = "$it\n", color = Color.Black)
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
                            TextInfo(text = stringResource(id = R.string.generation))
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
                                color = Color.Black
                            )
                        }
                    }
                }
            }
        }
    }
}
}

@Composable
fun DiscoveredImage(url: String){
    SubcomposeAsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(url)
            .build(),
        contentDescription = null,
        modifier = Modifier
            .requiredSize(200.dp)
    )
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



