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

    //con LIMIT 1 estraggo solo il primo elemento della lista
    @Query("SELECT * FROM Pokemon ORDER BY RANDOM() LIMIT 1")
    suspend fun getRandomPokemon(): Pokemon  //query da usare se vogliamo fare la funzionalità aggiuntiva della sfida

    @Query("SELECT * FROM Pokemon")
    fun getAll(): LiveData<MutableList<Pokemon>>

    @Query("SELECT * FROM Pokemon WHERE name LIKE :search")
    fun loadAllByTag(search: String): LiveData<MutableList<Pokemon>>

    @Query("SELECT * FROM Pokemon WHERE favorite = 1")
    fun loadAllByFavorite(): LiveData<MutableList<Pokemon>>

    @Update
    suspend fun favorite(item: Pokemon)

    @RawQuery(observedEntities = [Pokemon::class])
    fun getAllByTagAndFavorite(query: SupportSQLiteQuery): LiveData<MutableList<Pokemon>>

    fun getAllByTagAndFavoriteQuery(s: String, f: Int = 0): LiveData<MutableList<Pokemon>> {
        var query = "SELECT * FROM Pokemon WHERE name LIKE '%$s%'"
        if (f == 1)
            query += " AND favorite = 1"
        val simpleSQLiteQuery = SimpleSQLiteQuery(query, arrayOf<Pokemon>())
        return getAllByTagAndFavorite(simpleSQLiteQuery)
    }


}