package com.girlsintech.pokemon.screens.listpage

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.FilterList
import androidx.compose.material.icons.twotone.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.screens.fontBasic
import com.girlsintech.pokemon.ui.theme.BlackLight
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.util.parseGeneration
import com.girlsintech.pokemon.util.parseType
import com.girlsintech.pokemon.viewmodel.PokemonViewModel


@Composable
fun PokemonListPage(
    navController: NavController,
    viewModel: PokemonViewModel
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val configuration = LocalConfiguration.current

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
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
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











