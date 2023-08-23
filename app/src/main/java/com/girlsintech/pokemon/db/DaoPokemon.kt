package com.girlsintech.pokemon.db

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery

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

    @Query(
        """
        SELECT * FROM Pokemon
            WHERE favorite = 0
            ORDER BY RANDOM()
            LIMIT 1
    """
    )
    fun getRandomPokemon(): Pokemon?  //query per funzionalità aggiuntiva

    @RawQuery(observedEntities = [Pokemon::class])
    fun getImageFromName(query: SupportSQLiteQuery): LiveData<String>

    fun getImageFromNameQuery(s: String): LiveData<String> {
        val query = "SELECT img FROM Pokemon WHERE name LIKE '%$s%'"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query)
        return getImageFromName(simpleSQLiteQuery)
    }

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