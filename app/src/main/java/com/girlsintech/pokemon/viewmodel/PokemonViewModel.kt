package com.girlsintech.pokemon.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.*
import androidx.palette.graphics.Palette
import com.girlsintech.pokemon.db.DbPokemon
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.db.Repository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//classe responsabile per la gestione dei dati ottenuti dal database
class PokemonViewModel(application: Application) : AndroidViewModel(application) {

        private val repository: Repository
        var randomPokemon: Pokemon? = null

        init {
            val dao = DbPokemon.getInstance(application).pokemonDao()
            repository = Repository(dao)
        }

        // metodo per richiamare la query di ricerca dei Pokémon
        fun readByTag(s: String, f: Int, t: String, g: Int, a: String): LiveData<MutableList<Pokemon>> {
            return repository.readByTag("%$s%", f, "%$t%", g, "%$a%")
        }

        // metodo per richiamare la query che ricava l'immagine di un Pokémon dal nome
        fun getImageFromName(s: String): LiveData<String> {
            return repository.getImageFromName(s)
        }

        // metodo per richiamare la query che genera in maniera casuale un Pokémon
        fun getRandomPokemon() {
            viewModelScope.launch(Dispatchers.IO) {
                randomPokemon = repository.getRandomPokemon()
            }
        }

        // metodo per richiamare la query che modifica i dati di un Pokèmon nel database
        fun update(item: Pokemon) {
            viewModelScope.launch(Dispatchers.IO) {
                repository.update(item)
            }
        }

    // classe Factory incaricata di creare l'istanza del ViewModel
    @Suppress("UNCHECKED_CAST")
    class PokemonViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PokemonViewModel(application) as T
        }
    }

    // funzione per calcolare il colore dominante a partire da un'immagine
    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}

