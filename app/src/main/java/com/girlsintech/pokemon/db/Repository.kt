package com.girlsintech.pokemon.db

import androidx.lifecycle.LiveData

//classe che fornisce un’API per accedere ai dati nel database separando il codice dall’architettura
class Repository(private val dao: DaoPokemon) {

     fun update(pokemon: Pokemon) {
            dao.update(pokemon)
    }

    fun getRandomPokemon(): Pokemon? {
        return dao.getRandomPokemon()
    }

    fun readByTag(s: String, f: Int, t: String, g: Int, a: String): LiveData<MutableList<Pokemon>> {
        return dao.getAllByTagAndFavoriteQuery(s, f, t, g, a)
    }

    fun getImageFromName(s: String): LiveData<String> {
        return dao.getImageFromNameQuery(s)
    }
}