package com.girlsintech.pokemon.data.remote.model

import android.content.Context
import java.io.InputStream

//classe che mappa gli elementi nel file abilities.csv
class Ability {
    var id: String = "0"
    var en: String = "None"
    var it: String = "Nessuno"
}

abstract class ListOfAbilities {
    var abilities = mutableListOf<Ability>()
}

//uso di una classe Singleton per implementare la classe astratta ListOfAbilities in modo che ne venga creata una sola istanza
class SingletonListOfAbilities : ListOfAbilities() {
    companion object {
        private var listOfAbilities: SingletonListOfAbilities? = null

        fun getInstance(context: Context): SingletonListOfAbilities =
            listOfAbilities ?: synchronized(this) {
                listOfAbilities ?: readFile(context).also { listOfAbilities = it}
            }

        //metodo per leggere nel file abilities.csv e creare la lista con le abilità presenti all'interno
        private fun readFile(context: Context): SingletonListOfAbilities {
            val listOfAbilities = SingletonListOfAbilities()
            val assets = context.assets
            val inputStream: InputStream = assets.open("abilities.csv")

            inputStream.bufferedReader().useLines { sequence ->
                sequence.forEach {
                    val ability = Ability()
                    val fields = it.split(";")
                    ability.id = fields[0]
                    ability.en = fields[1]
                    ability.it = fields[2]
                    listOfAbilities.abilities.add(ability)
                }
            }
            return listOfAbilities
        }
    }
}