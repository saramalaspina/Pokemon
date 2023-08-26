package com.girlsintech.pokemon

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.girlsintech.pokemon.screens.detailpage.ErrorMessage
import com.girlsintech.pokemon.screens.detailpage.PokemonDetailPage
import com.girlsintech.pokemon.screens.discoverpage.PokemonDiscoverPage
import com.girlsintech.pokemon.screens.listpage.PokemonListPage
import com.girlsintech.pokemon.screens.startpage.MainView
import com.girlsintech.pokemon.ui.theme.PokemonTheme
import com.girlsintech.pokemon.util.SelectedPokemon
import com.girlsintech.pokemon.viewmodel.MyState
import com.girlsintech.pokemon.viewmodel.PokemonDetailViewModel
import com.girlsintech.pokemon.viewmodel.PokemonViewModel

//Activity in cui le attività vengono avviate tramite la funzione OnCreate utilizzata nella versione Compose
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            PokemonTheme {
                val context = LocalContext.current
                val owner = this

                //creazione tramite factory dei due ViewModel in associazione con l'attività
                val viewModel: PokemonViewModel =
                    viewModel(factory = PokemonViewModel.PokemonViewModelFactory(context.applicationContext as Application))

                val viewModelDetail: PokemonDetailViewModel =
                    viewModel(
                        factory = PokemonDetailViewModel.PokemonDetailViewModelFactory(context.applicationContext as Application)
                    )

                var refresh by rememberSaveable {
                    mutableStateOf(MyState.Load)
                }

                var message by rememberSaveable {
                    mutableStateOf("")
                }

                //per navigare da una schermata all'altra abbiamo utilizzato un Nav Controller
                val navController = rememberNavController()

                //definizione dei composable di tutte le schermate
                NavHost(
                    navController = navController,
                    startDestination = "pokemon_homepage"
                ) {
                    composable("pokemon_homepage") {
                        MainView(
                            {
                                navController.navigate("pokemon_discover_screen")
                            }, {
                                navController.navigate("pokemon_list_screen")
                            })
                    }
                    composable("pokemon_list_screen") {
                        PokemonListPage(navController = navController, viewModel = viewModel)
                    }
                    composable("pokemon_discover_screen") {
                        viewModel.getRandomPokemon()
                        PokemonDiscoverPage(navController = navController, viewModel = viewModel)
                    }
                    composable("pokemon_detail_screen") {

                        //tramite il metodo getData acquisisco le informazioni all'interno dell'url principale del Pokémon selezionato
                        viewModelDetail.getData(SelectedPokemon.pokemonSelected.value!!.url) {
                            refresh = MyState.Error
                            message = it
                        }

                        viewModelDetail.pokemonInfo.observe(owner) {
                            refresh = MyState.Success
                        }

                        //la pagina di dettaglio viene caricata quando l'acquisizione dei dati è andata a buon fine
                        when (refresh) {
                            MyState.Success -> {
                                PokemonDetailPage(
                                    dominantColor = SelectedPokemon.color.value,
                                    pokemon = SelectedPokemon.pokemonSelected.value!!,
                                    viewModel = viewModelDetail,
                                    viewModelDb = SelectedPokemon.viewModel.value!!,
                                    navController = navController
                                )
                            }
                            MyState.Error -> {
                                ErrorMessage(message)
                            }
                            MyState.Load, MyState.Init -> {}
                        }
                    }
                }
            }
        }
    }
}