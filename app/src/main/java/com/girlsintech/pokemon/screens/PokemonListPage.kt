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
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BlackLight
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*


@Composable
fun PokemonListPage(
    navController: NavController,
) {
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

        var refresh by rememberSaveable {
            mutableStateOf(false)
        }

        var onlyFavorite by rememberSaveable {
            mutableStateOf(false)
        }

        var isHintDisplayed by remember {
            mutableStateOf(false)
        }

        val pokemonList = viewModel.readByTag("%$filter%", if (onlyFavorite) 1 else 0)
            .observeAsState(listOf()).value

        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.sfondo),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds
        )

        Column {
            Spacer(modifier = Modifier.height(10.dp))
            myImage(R.drawable.pokemon_title)

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
                    fontFamily = newFont(),
                    textAlign = TextAlign . Center,
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

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
                        fontStyle = FontStyle.Italic
                    )
                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            PokemonList(
                list = pokemonList,
                refresh = refresh,
                viewModel = viewModel,
                navController = navController,
            ) {
                refresh = !it
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
            .padding(start = 15.dp, end = 15.dp)
            .background(CardBackground, RoundedCornerShape(10.dp))
    ) {
        LazyColumn (
            Modifier
                .padding(top =5.dp)
                ) {
            itemsIndexed(list) { index, pokemon ->
                ListItem(
                    text = {
                        PokemonItem(
                            pokemon = pokemon,
                            navController = navController,
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
    navController: NavController,
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
                    navController.navigate(
                        "pokemon_detail_screen/${dominantColor.toArgb()}/${pokemon.name}"
                    )
                }
                .fillMaxWidth()
        ) {
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
                        fontFamily = fontFamily(),
                        fontSize = 20.sp,
                        color = Color.White
                    )

                    Text(
                        text = pokemon.type,
                        fontFamily = fontFamily(),
                        fontSize = 15.sp,
                        color = Color.White
                    )
                }

                 SubcomposeAsyncImage(
                     model = ImageRequest.Builder(LocalContext.current)
                         .data(pokemon.img)
                         .diskCacheKey("pokemon_image_${pokemon.id}")
                         .listener { _, result ->
                             viewModel.calcDominantColor(result.drawable) { color ->
                             dominantColor = color
                            }
                         }
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


@Composable
fun SearchBar(
    hint: String = "",
    onSearch: (String) -> Unit
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = Modifier
        .background(CardBackground, CircleShape)
        .clip(RoundedCornerShape(20.dp))
        .padding(horizontal = 10.dp, vertical = 5.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        BasicTextField(
            value = text,
            onValueChange = {
                text = it
                onSearch(it)
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
                .padding(horizontal = 10.dp, vertical = 12.dp)
        )

        if (isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 35.dp, vertical = 12.dp)
            )
        }
    }
}









