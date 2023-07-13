package com.girlsintech.pokemon

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.girlsintech.pokemon.screens.MainView
import com.girlsintech.pokemon.screens.PokemonListPage
import com.girlsintech.pokemon.ui.theme.PokemonTheme
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonListViewModel
import com.girlsintech.pokemon.viewmodel.PokemonViewModelFactory
import kotlinx.coroutines.channels.Channel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonTheme {
                val navController = rememberNavController()
                val context = LocalContext.current

                val vm : PokemonListViewModel = viewModel(factory = PokemonViewModelFactory(context.applicationContext as Application))

                NavHost(navController = navController,
                    startDestination = "pokemon_homepage"){
                    composable("pokemon_homepage"){
                        MainView { navController.navigate("pokemon_list_screen") }
                    }
                    composable("pokemon_list_screen"){
                        PokemonListPage(navController = navController, viewModel = vm)
                    }
                    composable(
                        "pokemon_detail_screen/{dominantColor}/{pokemonName}",
                        arguments = listOf(
                            navArgument("dominantColor"){
                                type = NavType.IntType
                            },
                            navArgument("pokemonName"){
                                type = NavType.StringType
                            }
                        )
                    ){
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val pokemonName = remember {
                            it.arguments?.getString("pokemonName")
                        }
                    }
                }

            }
        }
    }
}