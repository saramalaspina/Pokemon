package com.girlsintech.pokemon.screens

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.ItemColor
import com.girlsintech.pokemon.viewmodel.PokemonViewModel
import java.util.*


@Composable
fun PokemonListPage(
    navController: NavController,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ){
        val context = LocalContext.current
        val viewModel : PokemonViewModel = viewModel(factory = PokemonViewModel.PokemonViewModelFactory(context.applicationContext as Application))

        var filter by rememberSaveable {
            mutableStateOf("")
        }

        var refresh by rememberSaveable {
            mutableStateOf(false)
        }

        var onlyFavorite by rememberSaveable {
            mutableStateOf(false)
        }

        val pokemonList = viewModel.readByTag("%$filter%", if (onlyFavorite) 1 else 0).observeAsState(listOf()).value

                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    myImage(R.drawable.pokemon_title)
                    SearchBar(
                        hint = "Search...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )

                    PokemonList(
                        list = pokemonList,
                        refresh = refresh,
                        viewModel = viewModel,
                        navController = navController,
                    ) {
                        refresh != it
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
    val defaultDominantColor = ItemColor
    var dominantColor by remember {
        mutableStateOf(defaultDominantColor)
    }

    LazyColumn {
        itemsIndexed(list) { index, pokemon ->
            ListItem (
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 5.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .background(defaultDominantColor)
                    .clickable {
                        navController.navigate(
                            "pokemon_detail_screen/${dominantColor.toArgb()}/${pokemon.name}"
                        )
                    }
                    .border(2.dp, BluePokemon),
                text = { Text(
                            text = pokemon.name.replaceFirstChar {
                                if (it.isLowerCase()) it.titlecase(
                                    Locale.ROOT
                                ) else it.toString()
                            },
                            fontFamily = fontFamily(),
                            fontSize = 18.sp,
                            modifier = Modifier.fillMaxSize() ,
                            textAlign = TextAlign.Center,
                        )
                    },

                secondaryText = {
                    Text(
                        text = "Type",
                        fontFamily = fontFamily(),
                        fontSize = 12.sp,
                        modifier = Modifier.fillMaxSize() ,
                        textAlign = TextAlign.Center
                    )
                },

                icon = {
                    AsyncImage(pokemon.img,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .fillMaxSize()
                    )
                },

                trailing = {
                    Row(horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically) {
                    }

                    IconButton(onClick = {
                            pokemon.favorite = 1- pokemon.favorite
                            viewModel.update(pokemon)
                            onRefresh(refresh)
                        }) {
                            Icon(
                                Icons.TwoTone.Favorite,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                                tint = if (pokemon.favorite == 1) Color.Red else Color.White,
                            )
                        }
                })
        }
    }
}



@Composable
fun SearchBar(
    modifier: Modifier = Modifier,
    hint: String = "",
    onSearch: (String) -> Unit = {}
) {
    var text by remember {
        mutableStateOf("")
    }
    var isHintDisplayed by remember {
        mutableStateOf(hint != "")
    }

    Box(modifier = modifier) {
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
                .padding(horizontal = 20.dp, vertical = 12.dp)
                .onFocusChanged {
                    isHintDisplayed = it.isFocused != true
                }
        )
        if(isHintDisplayed) {
            Text(
                text = hint,
                color = Color.LightGray,
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }
    }
}








