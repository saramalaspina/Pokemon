package com.girlsintech.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.toLowerCase
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.girlsintech.pokemon.screens.MainView
import com.girlsintech.pokemon.screens.PokemonDetailPage
import com.girlsintech.pokemon.screens.PokemonListPage
import com.girlsintech.pokemon.ui.theme.PokemonTheme
import java.util.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "pokemon_homepage"
                ) {
                    composable("pokemon_homepage") {
                        MainView {
                            navController.navigate("pokemon_list_screen")
                        }
                    }
                    composable("pokemon_list_screen") {
                        PokemonListPage(navController = navController)
                    }
                    composable(
                        "pokemon_detail_screen/{dominantColor}/{url}",
                        arguments = listOf(
                            navArgument("dominantColor") {
                                type = NavType.IntType
                            },
                            navArgument("url") {
                                type = NavType.StringType
                            }
                        )
                    ) {
                        val dominantColor = remember {
                            val color = it.arguments?.getInt("dominantColor")
                            color?.let { Color(it) } ?: Color.White
                        }
                        val url = remember {
                            it.arguments?.getString("url")
                        }
                        PokemonDetailPage(
                            dominantColor = dominantColor,
                            url = url ?: "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}