package com.girlsintech.pokemon.screens.discoverpage

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.model.SingletonListOfAbilities
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.screens.detailpage.TextInfo
import com.girlsintech.pokemon.screens.fontPokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.Discover
import com.girlsintech.pokemon.util.parseGenerationFromInt
import com.girlsintech.pokemon.util.parseType
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

                Timer().schedule(500) {
                    visibility = true
                }

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
                                    fontSize = 25.sp,
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
                                    fontSize = 25.sp,
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

@Composable
fun DiscoveredPokemon(
    pokemon: Pokemon,
    viewModel: PokemonViewModel,
    widthBox: Dp,
    namePadding: Dp,
    titlePadding: Dp
) {

    var fav by remember {
        mutableStateOf(pokemon.favorite)
    }
    var boxSize by remember {
        mutableStateOf(Size.Zero)
    }

    val scrollState = rememberScrollState()

    ConstraintLayout {

        val (card1, card2) = createRefs()

        Card(
            elevation = 15.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .constrainAs(card2) {
                    start.linkTo(card1.start, 6.dp)
                    top.linkTo(card1.top, 10.dp)
                }
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .width(with(LocalDensity.current) { boxSize.width.toDp() })
                    .height(with(LocalDensity.current) { boxSize.height.toDp() })
            )
        }

        Card(
            elevation = 5.dp,
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .constrainAs(card1) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                }
                .onGloballyPositioned { coordinates ->
                    boxSize = coordinates.size.toSize()
                }
        ) {
            Box(
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .background(
                        Brush.verticalGradient(
                            listOf(
                                Discover.copy(1f),
                                Discover.copy(0.9f),
                                Discover.copy(0.7f)
                            )
                        ), RoundedCornerShape(20.dp)
                    )
                    .width(widthBox)
            ) {

                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 25.dp, end = 25.dp)
                ) {
                    val (name, favorite, detail) = createRefs()

                    Text(
                        text = pokemon.name.replaceFirstChar {
                            if (it.isLowerCase()) it.titlecase(
                                Locale.ROOT
                            ) else it.toString()
                        },
                        modifier = Modifier
                            .constrainAs(name) {
                                top.linkTo(parent.top, namePadding)
                                start.linkTo(parent.start)
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
                                end.linkTo(parent.end)
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


                    Column(
                        modifier = Modifier
                            .padding(top = titlePadding, bottom = 30.dp)
                            .constrainAs(detail) {
                                top.linkTo(name.bottom)
                                start.linkTo(name.start)

                            },
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
                                            start.linkTo(parent.start)
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
                                        TextInfo(text = parseType(type = it), color = Color.White)
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
                                            start.linkTo(parent.start)
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
                                            if(Locale.getDefault().language == "en") {
                                                TextInfo(text = it, color = Color.White)
                                            } else {
                                                GetAbility(ability = it)
                                            }
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
                                            start.linkTo(parent.start)
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
                                        Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun GetAbility(
    ability: String
) {
    var abilityIt by remember {
        mutableStateOf("")
    }
    
    val listOfAbilities = SingletonListOfAbilities.getInstance(LocalContext.current)
    listOfAbilities.abilities.forEach {
        if (ability == it.en){
            abilityIt = it.it
        }
    }
    if(abilityIt.isNotBlank()) {
        TextInfo(text = abilityIt, color = Color.White)
    } else {
        TextInfo(text = ability, color = Color.White)
    }
}


@Composable
fun DiscoveredImage(
    url: String,
    modifier: Modifier
){

    Box(
        contentAlignment = Alignment.TopCenter
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(url)
                .build(),
            contentDescription = null,
            modifier = modifier
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



