package com.girlsintech.pokemon.db

import androidx.lifecycle.LiveData

//permette di creare thread per accedere al database senza toccare il main thread che non dovrebbe accedere al disco
class Repository(private val dao: DaoPokemon) {

     fun update(pokemon: Pokemon) {
            dao.update(pokemon)
    }

    fun readByTag(s: String, f: Int, t: String): LiveData<MutableList<Pokemon>> {
        return dao.getAllByTagAndFavoriteQuery(s, f, t) }

    fun getImageFromName(s: String): LiveData<String> {
        return dao.getImageFromNameQuery(s)
    }
}