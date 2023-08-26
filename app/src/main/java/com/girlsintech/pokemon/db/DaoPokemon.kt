package com.girlsintech.pokemon.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

//interfaccia @Dao per accedere ai dati nel database
@Dao
interface DaoPokemon {
    @Insert
    fun insertAll(pokemonList: List<Pokemon>)

    @Insert
    fun insert(pokemon: Pokemon)

    @Update
    fun update(pokemon: Pokemon)

    @Delete
    fun delete(pokemon: Pokemon)

    //query per generare in modo casuale un Pokémon nella funzionalità Discover
    @Query(
        """
        SELECT * FROM Pokemon
            WHERE favorite = 0
            ORDER BY RANDOM()
            LIMIT 1
    """
    )
    fun getRandomPokemon(): Pokemon?

    //query per ricavare l'url dell'immagine di un Pokémon conoscendo il nome
    @RawQuery(observedEntities = [Pokemon::class])
    fun getImageFromName(query: SupportSQLiteQuery): LiveData<String>

    fun getImageFromNameQuery(s: String): LiveData<String> {
        val query = "SELECT img FROM Pokemon WHERE name LIKE '%$s%'"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query)
        return getImageFromName(simpleSQLiteQuery)
    }

    //query usata per la ricerca filtrata della lista mostrata nel Pokédex
    @RawQuery(observedEntities = [Pokemon::class])
    fun getAllByTagAndFavorite(query: SupportSQLiteQuery): LiveData<MutableList<Pokemon>>

    fun getAllByTagAndFavoriteQuery(s: String, f: Int = 0, t: String, g: Int = 0, a: String): LiveData<MutableList<Pokemon>> {
        var query = "SELECT * FROM Pokemon WHERE name LIKE '%$s%' AND type LIKE '%$t%' AND ability LIKE '%$a%'"
        if (f == 1)
            query += " AND favorite = 1"
        if(g != 0)
            query += " AND generation = $g"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf<Pokemon>())
        return getAllByTagAndFavorite(simpleSQLiteQuery)
    }

}