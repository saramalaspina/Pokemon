package com.girlsintech.pokemon.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import com.girlsintech.pokemon.db.DbPokemon
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.db.Repository
import com.squareup.picasso.Picasso
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL

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

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}

