package com.girlsintech.pokemon.screens.listpage

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.material.icons.twotone.FilterList
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.screens.fontBasic
import com.girlsintech.pokemon.ui.theme.BlackLight
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.util.parseGeneration
import com.girlsintech.pokemon.util.parseType
import com.girlsintech.pokemon.viewmodel.PokemonViewModel

//pagina che mostra l'elenco completo dei pokemon
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

        var isDialogShown by rememberSaveable {
            mutableStateOf(false)
        }

        //ottengo la lista filtrata in base ai parametri inseriti
        val pokemonList = viewModel.readByTag(
            "%$filter%",
            if (onlyFavorite) 1 else 0,
            "%$type%",
            parseGeneration(generation),
            "%$abilityEn%"
        )
            .observeAsState(listOf()).value


        Row {
          /*  Icon(Icons.TwoTone.ArrowBack,
                contentDescription = null,
                tint = BluePokemon,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 15.dp, start = 15.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )*/

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
        }

        Column {

            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top
            ) {
                //la pagina viene composta diversamente a seconda dell'orientamento
                when (configuration.orientation) {
                    Configuration.ORIENTATION_PORTRAIT -> {
                        Image(
                            painter = painterResource(id = R.drawable.pokemon_title),
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
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
                            .size(33.dp),
                        contentDescription = null,
                        tint = if (onlyFavorite) Color.Red else Color.LightGray,
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = stringResource(id = R.string.favorites),
                        color = BlackLight,
                        fontStyle = FontStyle.Italic,
                        fontSize = 16.sp,
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
                            .size(28.dp)
                    )

                    Text(
                        text = stringResource(id = R.string.add_filters),
                        color = Color.Black,
                        modifier = Modifier
                            .clickable {
                                isDialogShown = true
                            },
                        fontSize = 16.sp,
                        fontStyle = FontStyle.Italic,
                        fontFamily = fontBasic()
                    )
                }


                if (isDialogShown) {

                    //invocazione dialog che mostra i filtri applicabili per la ricerca
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

            //invocazione della search bar per la ricerca basata sul nome
            SearchBar(filter = filter, onValueChange = { filter = it })

            Spacer(modifier = Modifier.height(10.dp))

            //invocazione della lista dei Pokémon da mostrare
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











