package com.girlsintech.pokemon.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//permette di creare thread per accedere al database senza toccare il main thread che non dovrebbe accedere al disco
class Repository(private val dao: DaoPokemon) {

    val readAllData: LiveData<MutableList<Pokemon>> = dao.getAll()

     fun update(pokemon: Pokemon) {
            dao.update(pokemon)
    }

    fun readByTag(s: String, f: Int): LiveData<MutableList<Pokemon>> {
        return dao.getAllByTagAndFavoriteQuery(s, f) }
}