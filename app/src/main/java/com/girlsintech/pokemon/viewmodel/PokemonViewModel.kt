package com.girlsintech.pokemon.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import com.girlsintech.pokemon.db.DbPokemon
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.db.Repository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PokemonViewModel(application: Application) : AndroidViewModel(application) {

        private val repository: Repository

        init {
            val dao = DbPokemon.getInstance(application).pokemonDao()
            repository = Repository(dao)
        }

        fun readByTag(s: String, f: Int): LiveData<MutableList<Pokemon>> {
            return repository.readByTag("%$s%", f)
        }

        fun update(item: Pokemon) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.update(item)
            }
        }

    @Suppress("UNCHECKED_CAST")
    class PokemonViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PokemonViewModel(application) as T
        }
    }

    fun calcDominantColor(url: String, onFinish: (Color) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            val imageBitmap = Picasso.get().load(url).get()

            Palette.from(imageBitmap).generate { palette ->
                palette?.dominantSwatch?.rgb?.let { colorValue ->
                    onFinish(Color(colorValue))
                }
            }
        }

    }
}