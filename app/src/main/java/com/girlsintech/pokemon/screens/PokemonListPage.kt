package com.girlsintech.pokemon.screens

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.twotone.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.model.Ability
import com.girlsintech.pokemon.data.remote.model.ListOfAbilities
import com.girlsintech.pokemon.data.remote.model.SingletonListOfAbilities
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BlackLight
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.util.Constants.IMAGE_URL
import com.girlsintech.pokemon.util.SelectedPokemon
import com.girlsintech.pokemon.util.parseGeneration
import com.girlsintech.pokemon.util.parseType
import com.girlsintech.pokemon.util.parseTypeIt
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*


@Composable
fun PokemonListPage(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val configuration = LocalConfiguration.current
        val context = LocalContext.current
        val viewModel: PokemonViewModel =
            viewModel(factory = PokemonViewModel.PokemonViewModelFactory(context.applicationContext as Application))

        var filter by remember {
            mutableStateOf("")
        }

        var type by rememberSaveable {
            mutableStateOf("")
        }

        var generation by rememberSaveable {
            mutableStateOf("")
        }

        var abilityIt by rememberSaveable {
            mutableStateOf("")
        }

        var abilityEn by rememberSaveable {
            mutableStateOf("")
        }

        var refresh by remember {
            mutableStateOf(false)
        }

        var onlyFavorite by rememberSaveable {
            mutableStateOf(false)
        }

        var isHintDisplayed by remember {
            mutableStateOf(false)
        }

        var isDialogShown by rememberSaveable {
            mutableStateOf(false)
        }


        val pokemonList = viewModel.readByTag(
            "%$filter%",
            if (onlyFavorite) 1 else 0,
            "%$type%",
            parseGeneration(generation),
            "%$abilityEn%"
        )
            .observeAsState(listOf()).value


        Row {
            Icon(Icons.TwoTone.ArrowBack,
                contentDescription = null,
                tint = BluePokemon,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 15.dp, start = 15.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )

            Image(
                modifier = Modifier.fillMaxSize(),
                painter = if (configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {
                    painterResource(id = R.drawable.sfondo)
                } else {
                    painterResource(id = R.drawable.sfondoh)
                },
                contentDescription = "background",
                contentScale = ContentScale.FillBounds
            )
        }

        Column {
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        Image(
                            painter = painterResource(id = R.drawable.pokemon_title),
                            contentDescription = null,
                            alignment = Alignment.TopCenter
                        )
                    }
                    else -> {
                        Image(
                            painter = painterResource(id = R.drawable.pokemon_title),
                            contentDescription = null,
                            alignment = Alignment.TopCenter,
                            modifier = Modifier
                                .requiredWidth(115.dp)
                                .offset(y = 15.dp)
                        )
                    }
                }
            }

            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (favorites, filters) = createRefs()

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .constrainAs(favorites) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 20.dp)
                        }
                ) {
                    Icon(
                        Icons.TwoTone.Favorite,
                        modifier = Modifier
                            .clickable {
                                onlyFavorite = !onlyFavorite
                            }
                            .size(35.dp),
                        contentDescription = null,
                        tint = if (onlyFavorite) Color.Red else Color.LightGray,
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = stringResource(id = R.string.favorites),
                        color = BlackLight,
                        fontStyle = FontStyle.Italic,
                        fontSize = 20.sp,
                        fontFamily = fontBasic(),
                        textAlign = TextAlign.Center,
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.spacedBy(7.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .constrainAs(filters) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end, 25.dp)
                        }
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.FilterList,
                        contentDescription = null,
                        modifier = Modifier
                            .size(30.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.add_filters),
                        color = Color.Black,
                        modifier = Modifier
                            .clickable {
                                isDialogShown = true
                            },
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = fontBasic()
                    )
                }


                if (isDialogShown) {

                    FilterDialog(
                        initAbilityEn = abilityEn,
                        initAbilityIt = abilityIt,
                        initType = parseType(type = type),
                        initGen = generation,
                        onDismiss = {
                            isDialogShown = false
                        },

                        onClickType = {
                            type = it
                        },
                        onClickGeneration = {
                            generation = it
                        },
                        onClickAbility = { en, it ->
                            if (en == "None") {
                                abilityEn = ""
                                abilityIt = ""
                            } else {
                                abilityEn = en
                                abilityIt = it
                            }
                        }
                    )
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .background(CardBackground, CircleShape)
                    .clip(RoundedCornerShape(20.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                BasicTextField(
                    value = filter,
                    onValueChange = {
                        filter = it
                    },
                    maxLines = 1,
                    singleLine = true,
                    textStyle = TextStyle(color = Color.Black),
                    modifier = Modifier
                        .fillMaxWidth()
                        .shadow(5.dp, CircleShape)
                        .background(Color.White, CircleShape)
                        .padding(horizontal = 45.dp, vertical = 12.dp)
                        .onFocusChanged {
                            isHintDisplayed = it.isFocused != true
                        }
                )

                Icon(
                    imageVector = Icons.TwoTone.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 12.dp)
                )

                Spacer(modifier = Modifier.width(15.dp))

                if (isHintDisplayed) {
                    Text(
                        text = stringResource(id = R.string.search),
                        color = Color.LightGray,
                        modifier = Modifier
                            .padding(horizontal = 50.dp, vertical = 12.dp),
                        fontSize = 18.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = fontBasic()
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            PokemonList(
                list = pokemonList,
                refresh = refresh,
                viewModel = viewModel,
                navController = navController
            ) {
                refresh = !it
            }
        }

    }
}

@Composable
fun AbilitySelection(
    initAbility: String,
    listOfAbilities: ListOfAbilities,
    onClickAbility: (Ability) -> Unit
) {
    val options = listOfAbilities.abilities

    var exp by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    var selection by remember {
        mutableStateOf(initAbility)
    }

    val icon = if (exp)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column(modifier = Modifier.width(205.dp)) {
        OutlinedTextField(
            value = selection,
            onValueChange = {
                selection = it
                exp = true
            },
            modifier = Modifier
                .width(205.dp)
                .height(50.dp)
                .shadow(5.dp, spotColor = CardBackground),
            enabled = true,
            keyboardActions = KeyboardActions { },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done   //tasto di spunta
            ),
            singleLine = true,

            textStyle = TextStyle(
                color = Color.Black,
                fontFamily = fontBasic(),
                textAlign = TextAlign.Start,
                fontSize = 15.sp
            ),
            shape = RoundedCornerShape(5.dp),

            colors = TextFieldDefaults.outlinedTextFieldColors(
                focusedBorderColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray,
                disabledBorderColor = Color.LightGray
            ),

            trailingIcon = {
                Icon(
                    icon,
                    contentDescription = null,
                    tint = BlackLight,
                    modifier = Modifier.clickable {
                        exp = !exp
                    }
                )
            },

            placeholder = {
                Text(
                    text = selection.ifBlank {
                        stringResource(id = R.string.search_ability)
                    },
                    fontSize = 15.sp,
                    fontFamily = fontBasic(),
                    color = Color.Black,
                    textAlign = TextAlign.Start
                )
            }
        )

        //colonne che non creano tutte le righe ma solo quelle visibili
        LazyColumn(
            modifier = Modifier
                .padding(top = 2.dp)
                .fillMaxWidth()
                .border(1.dp, Color.LightGray),
            contentPadding = PaddingValues(start = 10.dp)
        ) {
            val filterOpts = options.filter {
                if (Locale.getDefault().language == "en") {
                    it.en.startsWith(
                        selection,
                        ignoreCase = true  //evita il problema delle maiuscole e delle minuscole
                    )
                } else {
                    it.it.startsWith(
                        selection,
                        ignoreCase = true
                    )
                }
            }

            if (exp) {
                //lista di LazyColumn a cui passo la lista di oggetti
                itemsIndexed(filterOpts) { _, item ->
                    Text(
                        text = if (Locale.getDefault().language == "en") {
                            item.en
                        } else {
                            item.it
                        },
                        modifier = Modifier
                            .padding(4.dp)
                            .clickable(onClick = {
                                selection = if (item.id == "0") {
                                    ""
                                } else {
                                    if (Locale.getDefault().language == "en") {
                                        item.en
                                    } else {
                                        item.it
                                    }
                                }
                                onClickAbility(item)
                                exp = false //una volta cliccato la lista deve sparire
                                focusManager.clearFocus()
                            })
                    )
                }
            }
        }
    }
}

@Composable
fun FilterDialog(
    initAbilityEn: String,
    initAbilityIt: String,
    initType: String,
    initGen: String,
    onDismiss: () -> Unit,
    onClickType: (String) -> Unit,
    onClickGeneration: (String) -> Unit,
    onClickAbility: (String, String) -> Unit
) {

    val noneSelection = stringResource(id = R.string.none_m)

    var typeSelection by remember {
        mutableStateOf(initType)
    }

    var generationSelection by remember {
        mutableStateOf(initGen)
    }

    var abilitySelectionEn by remember {
        mutableStateOf(initAbilityEn)
    }

    var abilitySelectionIt by remember {
        mutableStateOf(initAbilityIt)
    }

    val types = listOf(
        stringResource(id = R.string.none_m),
        stringResource(id = R.string.normal),
        stringResource(id = R.string.fire),
        stringResource(id = R.string.water),
        stringResource(id = R.string.grass),
        stringResource(id = R.string.flying),
        stringResource(id = R.string.fighting),
        stringResource(id = R.string.poison),
        stringResource(id = R.string.electric),
        stringResource(id = R.string.ground),
        stringResource(id = R.string.rock),
        stringResource(id = R.string.psychic),
        stringResource(id = R.string.ice),
        stringResource(id = R.string.bug),
        stringResource(id = R.string.ghost),
        stringResource(id = R.string.steel),
        stringResource(id = R.string.dragon),
        stringResource(id = R.string.dark),
        stringResource(id = R.string.fairy)
    )

    val generations = listOf(
        stringResource(id = R.string.none_m),
        "I",
        "II",
        "III",
        "IV",
        "V",
        "VI",
        "VII",
        "VIII",
        "IX"
    )

    val listOfAbilities = SingletonListOfAbilities.getInstance(LocalContext.current)

    Dialog(
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Card(
            shape = RoundedCornerShape(10.dp),
            backgroundColor = Color.White,
            modifier = Modifier
                .height(400.dp)
                .width(330.dp)
                .shadow(10.dp, RoundedCornerShape(10.dp))
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Spacer(modifier = Modifier.height(30.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.FilterList,
                        contentDescription = null
                    )
                    Text(
                        text = stringResource(id = R.string.selection),
                        color = Color.Black,
                        fontSize = 20.sp,
                        fontFamily = fontBasic()
                    )
                }

                Spacer(modifier = Modifier.height(30.dp))

                AbilitySelection(
                    initAbility = if (Locale.getDefault().language == "en") {
                        abilitySelectionEn
                    } else {
                        abilitySelectionIt
                    },
                    listOfAbilities = listOfAbilities
                ) {
                    abilitySelectionEn = it.en
                    abilitySelectionIt = it.it
                }

                Spacer(modifier = Modifier.height(20.dp))

                FilterSelection(
                    itemList = generations,
                    currentSelection = generationSelection,
                    noneSelection = noneSelection,
                    selectionString = stringResource(id = R.string.selection_generation)
                ) {
                    generationSelection = if (it == noneSelection) {
                        ""
                    } else {
                        it
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                FilterSelection(
                    itemList = types,
                    currentSelection = typeSelection,
                    noneSelection = noneSelection,
                    selectionString = stringResource(id = R.string.selection_type)
                ) {
                    typeSelection = if (it == noneSelection) {
                        ""
                    } else {
                        it
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(backgroundColor = CardBackground),
                    modifier = Modifier
                        .width(160.dp),
                    onClick = {
                        if (Locale.getDefault().language == "en") {
                            onClickType(typeSelection)
                        } else {
                            onClickType(parseTypeIt(typeSelection))
                        }
                        onClickGeneration(generationSelection)
                        onClickAbility(abilitySelectionEn, abilitySelectionIt)
                        onDismiss()
                    }
                ) {
                    TextInfo(
                        text = stringResource(id = R.string.apply_filters),
                        Color.Black
                    )
                }
            }
        }
    }
}

@Composable
fun FilterSelection(
    itemList: List<String>,
    currentSelection: String,
    selectionString: String,
    noneSelection: String,
    onItemSelected: (String) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var selectedItem by rememberSaveable { mutableStateOf(currentSelection) }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedButton(
            onClick = { expanded = !expanded },
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                }
                //.background(CardBackground, RoundedCornerShape(10.dp))
                .border(BorderStroke(1.dp, Color.LightGray), RoundedCornerShape(5.dp))
                .height(50.dp)
        ) {
            Text(
                text =
                if (selectedItem == "") {
                    selectionString
                } else {
                    selectedItem
                },
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.width(150.dp),
                fontFamily = fontBasic(),
                color = Color.Black,
                fontSize = 15.sp,
                textAlign = TextAlign.Start,
            )
            Icon(icon, contentDescription = null, tint = BlackLight)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .width(with(LocalDensity.current) { textFieldSize.width.toDp() })
                .height(with(LocalDensity.current) { textFieldSize.height.toDp() + 255.dp })
        ) {
            itemList.forEach {
                DropdownMenuItem(onClick = {
                    expanded = false
                    selectedItem = it
                    onItemSelected(it)
                }) {
                    Text(text = it)
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonList(
    list: List<Pokemon>,
    refresh: Boolean,
    viewModel: PokemonViewModel,
    navController: NavController,
    onRefresh: (Boolean) -> Unit
) {

    Column(
        Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp)
            .background(CardBackground, RoundedCornerShape(10.dp))
    ) {
        LazyColumn(
            Modifier
                .padding(top = 5.dp)
        ) {
            itemsIndexed(list) { _, pokemon ->
                ListItem(
                    text = {
                        PokemonItem(
                            pokemon = pokemon,
                            refresh = refresh,
                            viewModel = viewModel,
                            navController = navController,
                            onRefresh = onRefresh
                        )
                    }
                )
            }
        }
    }
}

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
                    SelectedPokemon.selectPokemon(dominantColor, pokemon, viewModel)
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
                    .data("$IMAGE_URL$idImage.png")
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









