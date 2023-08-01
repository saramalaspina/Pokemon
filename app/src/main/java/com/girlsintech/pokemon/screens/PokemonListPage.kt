package com.girlsintech.pokemon.screens

import android.app.Application
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Search
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BlackLight
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.util.Constants.IMAGE_URL
import com.girlsintech.pokemon.util.ScreenRouter
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*


@Composable
fun PokemonListPage() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val context = LocalContext.current
        val viewModel: PokemonViewModel =
            viewModel(factory = PokemonViewModel.PokemonViewModelFactory(context.applicationContext as Application))

        var filter by rememberSaveable {
            mutableStateOf("")
        }

        var type by rememberSaveable {
            mutableStateOf("")
        }

        var refresh by rememberSaveable {
            mutableStateOf(false)
        }

        var onlyFavorite by rememberSaveable {
            mutableStateOf(false)
        }

        var isHintDisplayed by remember {
            mutableStateOf(false)
        }

        val types = listOf("None", "Normal", "Fire", "Water", "Grass", "Flying", "Fighting", "Poison",
            "Electric", "Ground", "Rock", "Psychic", "Ice", "Bug", "Ghost", "Steel", "Dragon", "Dark", "Fairy")

        val pokemonList = viewModel.readByTag("%$filter%", if (onlyFavorite) 1 else 0, "%$type%")
            .observeAsState(listOf()).value


Row {
    Icon(Icons.TwoTone.ArrowBack,
        contentDescription = null,
        tint = BluePokemon,
        modifier = Modifier
            .size(50.dp)
            .padding(top = 15.dp, start = 15.dp)
            .clickable {
            ScreenRouter.navigateTo(2,1)
            }
    )

    Image(
        modifier = Modifier.fillMaxSize(),
        painter = painterResource(id = R.drawable.sfondo),
        contentDescription = "background",
        contentScale = ContentScale.FillBounds
    )
}

        Column {
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center, verticalAlignment = Alignment.Top) {
                Image(
                    painter = painterResource(id = R.drawable.pokemon_title),
                    contentDescription = null,
                    alignment = Alignment.TopCenter
                )
            }

            Spacer(modifier = Modifier.height(3.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(start = 20.dp)
            ){
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
                    text = "Favorites",
                    color = BlackLight,
                    fontStyle = FontStyle.Italic,
                    fontSize = 20.sp,
                    fontFamily = fontBasic(),
                    textAlign = TextAlign . Center,
                )
                Spacer(modifier = Modifier.width(75.dp))

                TypeSelection(itemList = types) {
                    type = if (it == "None") {
                        ""
                    } else {
                        it
                    }
                }

            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(modifier = Modifier
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
                        .padding(horizontal = 35.dp, vertical = 12.dp)
                        .onFocusChanged {
                            isHintDisplayed = it.isFocused != true
                        }
                )

                Icon(imageVector = Icons.TwoTone.Search,
                    contentDescription = null,
                    modifier = Modifier
                        .padding(horizontal = 15.dp, vertical = 12.dp)
                )

                Spacer(modifier = Modifier.width(15.dp))

                if (isHintDisplayed) {
                    Text(
                        text = "Search...",
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
            ) {
                refresh = !it
            }
        }

    }
}

@Composable
fun TypeSelection(
    itemList: List<String>,
    onItemSelected: (selectedItem: String) -> Unit
) {
    var expanded by rememberSaveable { mutableStateOf(false) }

    var textFieldSize by remember { mutableStateOf(Size.Zero) }

    var selectedItem by remember { mutableStateOf("Select Type") }

    val icon = if (expanded)
        Icons.Filled.KeyboardArrowUp
    else
        Icons.Filled.KeyboardArrowDown

    Column {
        OutlinedButton(onClick = { expanded = !expanded },
            modifier = Modifier
                .onGloballyPositioned { coordinates ->
                    //This value is used to assign to the DropDown the same width
                    textFieldSize = coordinates.size.toSize()
                }
                .background(CardBackground, RoundedCornerShape(10.dp))
        ) {
            Text(
                text = selectedItem,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.width(110.dp),
                fontFamily = fontBasic(),
                color = Color.Black,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                fontStyle = FontStyle.Italic
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
                    selectedItem = if (it == "None") {
                        "Select Type"
                    } else {
                        it
                    }
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
    onRefresh: (Boolean) -> Unit
) {

    Column(
        Modifier
            .padding(start = 15.dp, end = 15.dp)
            .background(CardBackground, RoundedCornerShape(10.dp))
    ) {
        LazyColumn (
            Modifier
                .padding(top =5.dp)
                ) {
            itemsIndexed(list) { _, pokemon ->
                ListItem(
                    text = {
                        PokemonItem(
                            pokemon = pokemon,
                            refresh = refresh,
                            viewModel = viewModel,
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
    pokemon : Pokemon,
    refresh: Boolean,
    viewModel: PokemonViewModel,
    onRefresh: (Boolean) -> Unit
){
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
                    ScreenRouter.navigateToDetail(2, dominantColor, pokemon, viewModel)
                }
                .fillMaxWidth()
        ) {
            val idImage = if(pokemon.id > 1010) {
                pokemon.id + 8990
            }else {
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
                modifier = Modifier .alpha(0f)
            )

            ConstraintLayout {
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

                    Text(
                        text = pokemon.type,
                        fontFamily = fontPokemon(),
                        fontSize = 15.sp,
                        color = Color.White
                    )
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
                            start.linkTo(parent.start, 195.dp)
                            end.linkTo(icon.start, 9.dp)
                            bottom.linkTo(parent.bottom)
                        }
                        .size(90.dp)
                )

                Icon(
                    Icons.TwoTone.Favorite,
                    modifier = Modifier
                        .constrainAs(icon) {
                            top.linkTo(parent.top)
                            end.linkTo(parent.end)
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









