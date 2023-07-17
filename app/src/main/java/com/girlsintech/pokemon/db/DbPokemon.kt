package com.girlsintech.pokemon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Pokemon::class], version = 1)
abstract class DbPokemon : RoomDatabase() {
    abstract fun pokemonDao(): DaoPokemon

    companion object {
        private var db: DbPokemon? = null

        fun getInstance(context: Context): DbPokemon {
            //singleton
            if (db == null) {
                //devo creare il database
                db = Room.databaseBuilder(
                    context,
                    DbPokemon::class.java,
                    "pokemonDB.db"
                )
                    //ci permette di creare il database dal file
                    .createFromAsset("pokemonDB.db")
                    .build()
             }
            return db as DbPokemon
        }
    }
}