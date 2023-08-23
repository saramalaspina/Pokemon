package com.girlsintech.pokemon.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.Yellow
import com.girlsintech.pokemon.ui.theme.hpColor
import com.girlsintech.pokemon.viewmodel.MyState
import java.util.*
import kotlin.concurrent.schedule

@Composable
fun PokemonDiscoverPage(
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Yellow.copy(0.4f)
    ) {

        var discover by remember {
            mutableStateOf(MyState.Load)
        }

        Timer().schedule(4500) {
            discover = MyState.Success
        }

        when (discover) {
            MyState.Success -> {
                Icon(
                    Icons.TwoTone.ArrowBack,
                    contentDescription = null,
                    tint = BluePokemon,
                    modifier = Modifier
                        .requiredSize(33.dp)
                        .clickable {
                            navController.popBackStack()
                        }
                )
            }

            MyState.Error -> {}

            MyState.Load, MyState.Init -> {
                Loading()
            }
        }
    }
}
