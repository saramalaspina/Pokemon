package com.girlsintech.pokemon.screens

import android.app.Application
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel

@Composable
fun PokemonDetailPage (
    dominantColor: Color,
    url: String,
    navController: NavController
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        val context = LocalContext.current
        val viewModel: PokemonDetailViewModel =
            viewModel(
                factory = PokemonDetailViewModel.PokemonDetailViewModelFactory(context.applicationContext as Application))

        var refresh by rememberSaveable {
            mutableStateOf(MyState.Load)
        }
        var message by rememberSaveable {
            mutableStateOf("")
        }

        val pokemonInfo = viewModel.getData(url) {
            refresh = MyState.Error
            message = it
        }

        Text(text = "pokemon")

    }
}