package com.girlsintech.pokemon.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

//classe astratta @Database che estende RoomDatabase e definisce le entità e oggetti di accesso nel database
@Database(entities = [Pokemon::class], version = 1)
abstract class DbPokemon : RoomDatabase() {
    abstract fun pokemonDao(): DaoPokemon

    companion object {
        private var db: DbPokemon? = null

        //utilizzo della classe Singleton per la creazione di un'unica istanza
        fun getInstance(context: Context): DbPokemon {
            //singleton
            if (db == null) {

                //metodo per acquisire l'istanza di database in fase di esecuzione
                db = Room.databaseBuilder(
                    context,
                    DbPokemon::class.java,
                    "pokemonDB.db"
                )
                    //ci permette di creare il database dal file SQLite DB nel folder assets
                    .createFromAsset("pokemonDB.db")
                    .build()
             }
            return db as DbPokemon
        }
    }
}