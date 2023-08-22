package com.girlsintech.pokemon.screens

import android.annotation.SuppressLint
import android.content.res.Configuration
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.ArrowForward
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.airbnb.lottie.compose.*
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.ability.AbilityDescription
import com.girlsintech.pokemon.data.remote.ability.FlavorTextEntry
import com.girlsintech.pokemon.data.remote.evolution.Evolution
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.data.remote.species.Species
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.util.*
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*
import kotlin.concurrent.schedule
import kotlin.math.roundToInt

@SuppressLint("UnrememberedMutableState", "MutableCollectionMutableState")
@Composable
fun PokemonDetailPage(
    dominantColor: Color,
    pokemon: Pokemon,
    viewModel: PokemonDetailViewModel,
    viewModelDb: PokemonViewModel,
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = dominantColor.copy(0.6f)
    ) {

        var pokemonSpecies: Species? by remember {
            mutableStateOf(null)
        }

        var evolutionChain: Evolution? by remember {
            mutableStateOf(null)
        }

        var ability1: AbilityDescription? by remember {
            mutableStateOf(null)
        }

        var ability2: AbilityDescription? by remember {
            mutableStateOf(null)
        }

        var ability3: AbilityDescription? by remember {
            mutableStateOf(null)
        }

        var refresh by remember {
            mutableStateOf(MyState.Load)
        }

        var refreshEvolution by remember {
            mutableStateOf(MyState.Load)
        }

        var navState by remember {
            mutableStateOf(0)
        }

        var message by remember {
            mutableStateOf("")
        }

        val pokemonInfo = viewModel.pokemonInfo.observeAsState().value

        val numAbility = pokemonInfo!!.abilities.size


        viewModel.getAbility(
            pokemonInfo.abilities[0].ability.url,
            {
                refresh = MyState.Error
                message = it
            },
            {
                ability1 = it
            }
        )

        if (numAbility > 1) {
            viewModel.getAbility(
                pokemonInfo.abilities[1].ability.url,
                {
                    refresh = MyState.Error
                    message = it
                },
                {
                    ability2 = it
                }
            )
        }

        if (numAbility > 2) {
            viewModel.getAbility(
                pokemonInfo.abilities[2].ability.url,
                {
                    refresh = MyState.Error
                    message = it
                },
                {
                    ability3 = it
                }
            )
        }

        viewModel.getSpecies(
            pokemonInfo.species.url,
            {
                refreshEvolution = MyState.Error
                message = it
            },
            {
                pokemonSpecies = it
                refreshEvolution = MyState.Success
            }
        )

        when (refreshEvolution) {
            MyState.Success -> {
                viewModel.getEvolution(pokemonSpecies!!.evolution_chain.url,
                    {
                        refresh = MyState.Error
                        message = it
                    },
                    {
                        evolutionChain = it
                        Timer().schedule(1500) {
                            if (refresh != MyState.Error) {
                                refresh = MyState.Success
                            }
                        }
                    }
                )
            }

            MyState.Error -> {
                ErrorMessage(message = message)
            }

            MyState.Load, MyState.Init -> {}
        }

        val configuration = LocalConfiguration.current

        when (refresh) {
            MyState.Success -> {

                when (configuration.orientation) {

                    Configuration.ORIENTATION_PORTRAIT -> {

                        Column {
                            TopIcons(
                                pokemon = pokemon,
                                viewModel = viewModelDb,
                                navController = navController
                            )

                            TopBox(
                                pokemonInfo = pokemonInfo,
                                dominantColor
                            )
                        }

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxSize()
                                .offset(y = 330.dp)
                                .background(Color.White, RoundedCornerShape(10))
                        ) {
                            Spacer(modifier = Modifier.height(110.dp))

                            NavigationBar {
                                navState = it
                            }

                            Spacer(modifier = Modifier.height(25.dp))

                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {

                                when (navState) {
                                    0 -> PokemonDetailSection(
                                        dominantColor = dominantColor,
                                        pokemonInfo = pokemonInfo,
                                        pokemonSpecies = pokemonSpecies!!,
                                        ability1 = ability1,
                                        ability2 = ability2,
                                        ability3 = ability3
                                    )
                                    1 -> PokemonStatSection(pokemonInfo = pokemonInfo)
                                    2 -> PokemonEvolutionSection(
                                        viewModelDb = viewModelDb,
                                        evolution = evolutionChain!!,
                                        330.dp
                                    )
                                }
                            }
                        }
                        ImageBox(
                            pokemon.img,
                            modifier = Modifier
                                .size(250.dp)
                                .offset(y = 180.dp),
                            Alignment.TopCenter
                        )
                    }
                    else -> {
                        ConstraintLayout {
                            val (icons, image, name, detail) = createRefs()

                            Column (modifier = Modifier
                                .constrainAs(icons){
                                    top.linkTo(parent.top, 10.dp)
                                    start.linkTo(parent.start, 5.dp)
                                }) {
                                TopIcons(
                                    pokemon = pokemon,
                                    viewModel = viewModelDb,
                                    navController = navController
                                )
                            }

                            Column (modifier = Modifier
                                .constrainAs(name){
                                    top.linkTo(icons.bottom)
                                    start.linkTo(parent.start, 30.dp)
                                }) {
                                TopBox(
                                    pokemonInfo = pokemonInfo,
                                    dominantColor,
                                )
                            }

                            Column (modifier = Modifier
                                .constrainAs(image){
                                    top.linkTo(name.bottom)
                                    start.linkTo(parent.start, 30.dp)
                                    bottom.linkTo(parent.bottom)
                                }){
                                ImageBox(
                                    pokemon.img,
                                    modifier = Modifier
                                        .size(215.dp)
                                        .offset(x = 15.dp),
                                    Alignment.CenterStart
                                )
                            }


                            Column(
                                horizontalAlignment = Alignment.Start,
                                modifier = Modifier
                                    .width(460.dp)
                                    .fillMaxHeight()
                                    .background(Color.White, RoundedCornerShape(10))
                                    .constrainAs(detail) {
                                        top.linkTo(parent.top, 30.dp)
                                        start.linkTo(parent.start, 310.dp)
                                    }
                            ) {

                                Spacer(modifier = Modifier.height(25.dp))

                                NavigationBar {
                                    navState = it
                                }

                                Spacer(modifier = Modifier.height(25.dp))

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {

                                    when (navState) {
                                        0 -> PokemonDetailSection(
                                            dominantColor = dominantColor,
                                            pokemonInfo = pokemonInfo,
                                            pokemonSpecies = pokemonSpecies!!,
                                            ability1 = ability1,
                                            ability2 = ability2,
                                            ability3 = ability3
                                        )
                                        1 -> PokemonStatSection(pokemonInfo = pokemonInfo)
                                        2 -> PokemonEvolutionSection(
                                            viewModelDb = viewModelDb,
                                            evolution = evolutionChain!!,
                                            20.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
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
fun TopIcons(
    pokemon: Pokemon,
    viewModel: PokemonViewModel,
    navController: NavController
) {
    var fav by remember {
        mutableStateOf(pokemon.favorite)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        ConstraintLayout (modifier = Modifier.fillMaxWidth()) {
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
                    .requiredSize(33.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Icon(
                Icons.TwoTone.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(favorite) {
                        top.linkTo(parent.top, 2.dp)
                        end.linkTo(parent.end, 25.dp)
                        bottom.linkTo(parent.bottom)
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
    }
}

@Composable
fun TopBox(
    pokemonInfo: PokemonInfo,
    dominantColor: Color,
) {

    Column {

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
                    pokemonInfo.types.forEach {
                        Text(
                            text = parseType(type = it.type.name),
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
    imgUrl: String,
    modifier: Modifier,
    alignment: Alignment
) {
    Box(
        contentAlignment = alignment
    ) {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(imgUrl)
                .build(),
            contentDescription = null,
            modifier = modifier
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
fun PokemonEvolutionSection(
    viewModelDb: PokemonViewModel,
    evolution: Evolution,
    bottomPadding: Dp
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(start = 25.dp, end = 25.dp, bottom = bottomPadding)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val evolutionFirst = evolution.chain.species.name

        if(evolution.chain.evolves_to.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_evolution),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                textAlign = TextAlign.Center,
                fontFamily = fontBasic(),
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
                color = Color.DarkGray
            )
        }

        evolution.chain.evolves_to.forEach { evolves_to ->

            val evolutionSecond = evolves_to.species.name

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EvolutionBox(viewModelDb = viewModelDb, text = evolutionFirst)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.TwoTone.ArrowForward,
                        contentDescription = null,
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .requiredSize(30.dp)
                    )
                    TextInfo(text = evolves_to.evolution_details[0].trigger.name)
                }
                EvolutionBox(viewModelDb = viewModelDb, text = evolutionSecond)
            }
            evolves_to.evolves_to.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EvolutionBox(viewModelDb = viewModelDb, text = evolutionSecond)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.TwoTone.ArrowForward,
                            contentDescription = null,
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .requiredSize(30.dp)
                        )
                        TextInfo(text = it.evolution_details[0].trigger.name)
                    }
                    EvolutionBox(viewModelDb = viewModelDb, text = it.species.name)
                }
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
    }
}

@Composable
fun EvolutionBox(
    viewModelDb: PokemonViewModel,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(CardBackground, RoundedCornerShape(100))
                .requiredSize(115.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        viewModelDb.getImageFromName(text).observeAsState().value
                    )
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .requiredSize(95.dp)
            )
        }

        TextInfo(text = text, Color.Black)
    }
}


@Composable
fun PokemonDetailSection(
    dominantColor: Color,
    pokemonInfo: PokemonInfo,
    pokemonSpecies: Species,
    ability1: AbilityDescription?,
    ability2: AbilityDescription?,
    ability3: AbilityDescription?
) {
    var isDialogShown by remember {
        mutableStateOf(false)
    }

    var abilityDescription by rememberSaveable {
        mutableStateOf("")
    }

    var ability: AbilityDescription? by remember {
        mutableStateOf(null)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (detailCol1, detailCol2) = createRefs()

            Column(
                modifier = Modifier.
                constrainAs(detailCol1) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top)
                },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextInfo(text = stringResource(id = R.string.species))
                TextInfo(text = stringResource(id = R.string.height))
                TextInfo(text = stringResource(id = R.string.weight))
                TextInfo(text = stringResource(id = R.string.abilities))
            }

                Column(
                    modifier = Modifier.
                    constrainAs(detailCol2) {
                        start.linkTo(parent.start, 115.dp)
                        top.linkTo(parent.top)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    pokemonSpecies.genera.forEach {
                        if (it.language.name == Locale.getDefault().language) {
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

                    Column(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        ability1!!.names.forEach { name ->
                            if (name.language.name == Locale.getDefault().language) {
                                Text(
                                    text = name.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    },
                                    color = Color.Black,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 15.sp,
                                    fontFamily = fontBasic(),
                                    modifier = Modifier
                                        .clickable {
                                            isDialogShown = true
                                            ability = ability1
                                            abilityDescription = ability1.name
                                        }
                                )
                            }
                        }

                        ability2?.names?.forEach { name ->
                            if (name.language.name == Locale.getDefault().language) {
                                Text(
                                    text = name.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    },
                                    color = Color.Black,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 15.sp,
                                    fontFamily = fontBasic(),
                                    modifier = Modifier
                                        .clickable {
                                            isDialogShown = true
                                            ability = ability2
                                            abilityDescription = ability2.name
                                        }
                                )
                            }
                        }

                        ability3?.names?.forEach { name ->
                            if (name.language.name == Locale.getDefault().language) {
                                Text(
                                    text = name.name.replaceFirstChar {
                                        if (it.isLowerCase()) it.titlecase(
                                            Locale.ROOT
                                        ) else it.toString()
                                    },
                                    color = Color.Black,
                                    fontStyle = FontStyle.Italic,
                                    fontSize = 15.sp,
                                    fontFamily = fontBasic(),
                                    modifier = Modifier
                                        .clickable {
                                            isDialogShown = true
                                            ability = ability3
                                            abilityDescription = ability3.name
                                        }
                                )
                            }
                        }
                    }
                }
            }
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ConstraintLayout(
                modifier = Modifier.fillMaxWidth()
            ) {
                val (detailCol1, detailCol2) = createRefs()
            Column(
                modifier = Modifier.
                constrainAs(detailCol1) {
                    start.linkTo(parent.start)
                    top.linkTo(parent.top, 20.dp)
                },
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                TextInfo(text = stringResource(id = R.string.generation))
                TextInfo(text = stringResource(id = R.string.growth_rate))
                TextInfo(text = stringResource(id = R.string.egg_groups))
            }

                Column(
                    modifier = Modifier.constrainAs(detailCol2) {
                        start.linkTo(parent.start, 115.dp)
                        top.linkTo(parent.top, 20.dp)
                    },
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    val gen = pokemonSpecies.generation.name.split("-")[1]
                    TextInfo(text = gen.uppercase(), Color.Black)

                    TextInfo(
                        text = parseGrowthRate(growthRate = (pokemonSpecies.growth_rate.name)),
                        Color.Black
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        pokemonSpecies.egg_groups.forEach {
                            TextInfo(text = parseEggGroups(eggGroup = it.name), Color.Black)
                        }
                    }
                }
            }
        }

        if (isDialogShown) {
            AbilityDialog(
                dominantColor = dominantColor,
                onDismiss = { isDialogShown = false },
                ability = ability!!
            )
        }
    }
}

@Composable
fun AbilityDialog(
    dominantColor: Color,
    onDismiss: () -> Unit,
    ability: AbilityDescription
) {
    var flavorEntry: FlavorTextEntry? by remember {
        mutableStateOf(null)
    }
    Dialog(
        onDismissRequest = { onDismiss() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(110.dp)
                .fillMaxWidth()
                .shadow(10.dp, RoundedCornerShape(10.dp))
                .background(Color.White, RoundedCornerShape(10.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxSize()
                    .background(dominantColor.copy(0.5f), RoundedCornerShape(10.dp))
            ) {
                ability.flavor_text_entries.forEach {
                    if (it.language.name == Locale.getDefault().language) {
                        flavorEntry = it
                    }
                }
                Row (modifier = Modifier.padding(horizontal = 10.dp)) {
                    Text(
                        text = flavorEntry!!.flavor_text,
                        color = Color.Black,
                        fontFamily = fontBasic(),
                        textAlign = TextAlign.Center,
                        fontSize = 15.sp,
                        fontStyle = FontStyle.Italic
                    )
                }
            }
        }
    }
}



@Composable
fun NavigationBar(
    onClick: (Int) -> Unit
) {
    var selectedAbout by rememberSaveable {
        mutableStateOf(true)
    }

    var selectedStats by rememberSaveable {
        mutableStateOf(false)
    }

    var selectedEvolution by rememberSaveable {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier
                .clickable {
                    selectedAbout = true
                    selectedStats = false
                    selectedEvolution = false
                    onClick(0)
                }
        ) {
            Text(
                text = stringResource(id = R.string.about),
                fontFamily = fontBasic(),
                color = if (selectedAbout) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedAbout) FontWeight.Bold else FontWeight(10)
            )
        }
        Column(
            modifier = Modifier
                .clickable {
                    selectedStats = true
                    selectedAbout = false
                    selectedEvolution = false
                    onClick(1)
                }
        ) {
            Text(
                text = stringResource(id = R.string.stats),
                fontFamily = fontBasic(),
                color = if (selectedStats) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedStats) FontWeight.Bold else FontWeight(10)
            )
        }
        Column(
            modifier = Modifier
                .clickable {
                    selectedEvolution = true
                    selectedAbout = false
                    selectedStats = false
                    onClick(2)
                }
        ) {
            Text(
                text = stringResource(id = R.string.evolutions),
                fontFamily = fontBasic(),
                color = if (selectedEvolution) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedEvolution) FontWeight.Bold else FontWeight(10)
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
        Text(
            text = stringResource(id = R.string.loading),
            modifier = Modifier
            .constrainAs(wp) {
                top.linkTo(pi.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            fontFamily = fontBasic()
        )
    }
}
