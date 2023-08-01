package com.girlsintech.pokemon

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.girlsintech.pokemon.screens.*
import com.girlsintech.pokemon.ui.theme.PokemonTheme
import com.girlsintech.pokemon.util.Constants.SPECIES_URL
import com.girlsintech.pokemon.util.ScreenRouter
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonTheme {
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

                when(ScreenRouter.currentScreen.value) {
                    1 -> MainView {
                        ScreenRouter.navigateTo(1, 2)
                    }
                    2 -> PokemonListPage()
                    3 -> {
                        viewModel.getData(ScreenRouter.pokemonSelected.value!!.url) {
                            refresh = MyState.Error
                            message = it
                        }
                        viewModel.pokemonInfo.observe(this) {
                            refresh = MyState.Success
                        }

                        when (refresh) {
                            MyState.Success -> {
                                PokemonDetailPage(
                                    dominantColor = ScreenRouter.color.value,
                                    pokemon = ScreenRouter.pokemonSelected.value!!,
                                    viewModel = viewModel,
                                    viewModelDb = ScreenRouter.viewModel.value!!
                                )
                            }
                            MyState.Error -> {
                                ErrorMessage(message)
                            }
                            MyState.Load, MyState.Init -> {
                                Loading()
                            }
                        }
                    }
                }
            }
        }
    }
}