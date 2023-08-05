package com.girlsintech.pokemon.screens

import android.annotation.SuppressLint
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.evolution.Evolution
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.data.remote.species.Species
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.util.ScreenRouter
import com.girlsintech.pokemon.util.parseStatToAbbr
import com.girlsintech.pokemon.util.parseStatToColor
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.roundToInt

@SuppressLint("UnrememberedMutableState")
@Composable
fun PokemonDetailPage(
    dominantColor: Color,
    pokemon: Pokemon,
    viewModel: PokemonDetailViewModel,
    viewModelDb: PokemonViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = dominantColor.copy(0.6f)
    ) {
        var pokemonSpecies: Species? by rememberSaveable {
            mutableStateOf(null)
        }

        var evolutionChain: Evolution? by rememberSaveable{
            mutableStateOf(null)
        }

        val scrollState = rememberScrollState()

        var refresh by rememberSaveable {
            mutableStateOf(MyState.Load)
        }

        var refreshEvolution by rememberSaveable {
            mutableStateOf(MyState.Load)
        }

        var navState by rememberSaveable {
            mutableStateOf(0)
        }

        var message by rememberSaveable {
            mutableStateOf("")
        }

        var pokemonInfo = viewModel.pokemonInfo.observeAsState().value

        viewModel.getSpecies(pokemonInfo!!.species.url,
            {
                refresh = MyState.Error
                message = it
            },
            {
                pokemonSpecies = it
                refreshEvolution = MyState.Success
            }
        )

        when(refreshEvolution) {
            MyState.Success -> {
                viewModel.getEvolution(pokemonSpecies!!.evolution_chain.url,
                    {
                        refresh = MyState.Error
                        message = it
                    },
                    {
                        evolutionChain = it
                        Timer().schedule(1500) {
                            refresh = MyState.Success
                        }
                    }
                )
            }

            MyState.Error -> {
                ErrorMessage(message = message)
            }

            MyState.Load, MyState.Init -> {}
        }


        when (refresh) {
            MyState.Success -> {

                TopBox(pokemonInfo = pokemonInfo, pokemon, dominantColor, viewModelDb)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxSize()
                        .offset(y = 330.dp)
                        .verticalScroll(scrollState)
                        .background(Color.White, RoundedCornerShape(10))
                ) {
                    Spacer(modifier = Modifier.height(110.dp))

                    NavigationBar {
                        navState = 1 - navState
                    }

                    Spacer(modifier = Modifier.height(25.dp))

                    when (navState) {
                        0 -> PokemonDetailSection(
                            pokemonInfo = pokemonInfo,
                            pokemonSpecies = pokemonSpecies!!,
                            evolution = evolutionChain!!
                        )
                        1 -> PokemonStatSection(pokemonInfo = pokemonInfo)
                    }
                }
                ImageBox(pokemon.img)
            }

            MyState.Error -> {
                ErrorMessage(message)
            }
            MyState.Load, MyState.Init -> {
                Loading()
            }
        }
    }
}

@Composable
fun PokemonDetailStats(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )
    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }
    Box(
        modifier = Modifier
            .padding(start = 25.dp, end = 45.dp)
            .fillMaxWidth()
            .height(25.dp)
            .clip(CircleShape)
            .background(
                if (isSystemInDarkTheme()) {
                    Color(0xFF505050)
                } else {
                    Color.LightGray
                }
            )
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(25.dp)
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 10.dp)
        ) {
            Text(
                text = statName,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                fontFamily = fontBasic()
            )
            Text(
                text = (curPercent.value * statMaxValue).toInt().toString(),
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                fontFamily = fontBasic()
            )
        }
    }
}

@Composable
fun TopBox(
    pokemonInfo: PokemonInfo,
    pokemon: Pokemon,
    dominantColor: Color,
    viewModel: PokemonViewModel
) {
    var fav by remember {
        mutableStateOf(pokemon.favorite)
    }

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
                    modifier = Modifier
                        .constrainAs(favorite) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 340.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .requiredSize(30.dp)
                        .clickable {
                            pokemon.favorite = 1 - pokemon.favorite
                            fav = 1 - fav
                            viewModel.update(pokemon)
                        }
                        .size(30.dp),
                    tint = if (fav == 1) Color.Red else Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        Box {

            Column (modifier = Modifier.padding(start = 25.dp, top = 5.dp)){
                Text(
                    text = "N° ${pokemonInfo.id}",
                    fontFamily = fontPokemon(),
                    fontSize = 25.sp,
                    color = Color.White
                )

                Text(
                    text = pokemonInfo.name.replaceFirstChar {
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
                    pokemonInfo.types.forEach { s ->
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
fun PokemonStatSection(
    pokemonInfo: PokemonInfo
) {
    pokemonInfo.stats.forEach { stat ->
        Spacer(modifier = Modifier.height(20.dp))
        PokemonDetailStats(
            statName = parseStatToAbbr(stat),
            statValue = stat.base_stat,
            statMaxValue = pokemonInfo.stats.maxOf { it.base_stat },
            statColor = parseStatToColor(stat),
            animDelay = 500
        )
    }
}



@Composable
fun PokemonDetailSection(
    pokemonInfo: PokemonInfo,
    pokemonSpecies: Species,
    evolution: Evolution
) {
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
            pokemonSpecies.genera.forEach {
                if (it.language.name.equals("en")) {
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

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pokemonInfo.abilities.forEach {
                    TextInfo(text = it.ability.name, Color.Black)
                }
            }
        }
    }
    Spacer(modifier = Modifier.padding(10.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
    ) {
        Text(
            text = "Evolution",
            fontSize = 20.sp,
            color = Color.Black,
            fontFamily = fontBasic(),
            fontWeight = FontWeight.Bold
        )
    }
    Spacer(modifier = Modifier.padding(5.dp))
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextInfo(text = "Generation")
            TextInfo(text = "Growth rate")
            TextInfo(text = "Egg groups")
            TextInfo(text = "Chain")
        }

        Spacer(modifier = Modifier.width(38.dp))

        Column(
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            TextInfo(text = pokemonSpecies.generation.name, Color.Black)
            TextInfo(text = pokemonSpecies.growth_rate.name, Color.Black)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                pokemonSpecies.egg_groups.forEach {
                    TextInfo(text = it.name, Color.Black)
                }
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                TextInfo(text = evolution.chain.species.name, Color.Black)
                evolution.chain.evolves_to.forEach { evolves_to ->
                    TextInfo(text = " -> " + evolves_to.species.name, Color.Black)
                    evolves_to.evolves_to.forEach{
                        TextInfo(text = " -> " + it.species.name, Color.Black)
                    }
                }
            }
        }
    }
}


@Composable
fun NavigationBar(
    onClick: () -> Unit
)
{
    var selectedAbout by rememberSaveable {
        mutableStateOf(true)
    }

    var selectedStats by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
    ){
        Column(
            modifier = Modifier
                .padding(start = 25.dp)
                .clickable {
                    selectedAbout = !selectedAbout
                    selectedStats = false
                    onClick()
                }
        ) {
            Text(
                text = "About",
                fontFamily = fontBasic(),
                color = if (selectedAbout) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedAbout) FontWeight.Bold else FontWeight(10)
            )
        }
        Spacer(modifier = Modifier.width(35.dp))
        Column(
            modifier = Modifier
                .clickable {
                    selectedStats = !selectedStats
                    selectedAbout = false
                    onClick()
                }
        ) {
            Text(
                text = "Base Stats",
                fontFamily = fontBasic(),
                color = if (selectedStats) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedStats) FontWeight.Bold else FontWeight(10)
            )
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
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.pokeball_animation))
    val progress by animateLottieCompositionAsState(
        composition = composition,
        iterations = LottieConstants.IterateForever
    )
    ConstraintLayout {
        val (pi, wp) = createRefs()
        LottieAnimation(modifier = Modifier
            .size(50.dp)
            .constrainAs(pi) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                bottom.linkTo(parent.bottom)
                end.linkTo(parent.end)
            },
            composition = composition, progress = {progress})
        Text(text = "Wait, please", modifier = Modifier
            .constrainAs(wp) {
                top.linkTo(pi.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontFamily = fontBasic()
        )
    }
}
