package com.girlsintech.pokemon.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.girlsintech.pokemon.db.DbPokemon
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.db.Repository
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
}