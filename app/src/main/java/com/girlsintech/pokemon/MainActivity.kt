package com.girlsintech.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

import com.girlsintech.pokemon.screens.MainView
import com.girlsintech.pokemon.screens.PokemonDetailPage
import com.girlsintech.pokemon.screens.PokemonListPage
import com.girlsintech.pokemon.ui.theme.PokemonTheme

import com.girlsintech.pokemon.util.ScreenRouter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonTheme {

                when(ScreenRouter.currentScreen.value) {
                    1 -> MainView {
                        ScreenRouter.navigateTo(1, 2)
                    }
                    2 -> PokemonListPage()
                    3 -> PokemonDetailPage(dominantColor = ScreenRouter.color.value, url = ScreenRouter.url.value)
                }
            }
        }
    }
}