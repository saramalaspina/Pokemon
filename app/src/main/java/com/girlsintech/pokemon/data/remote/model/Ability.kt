package com.girlsintech.pokemon.data.remote.model

import android.content.Context
import java.io.InputStream

class Ability {
    var id: String = "0"
    var en: String = "None"
    var it: String = "Nessuno"
}

abstract class ListOfAbilities {
    var abilities = mutableListOf<Ability>()
}

class SingletonListOfAbilities : ListOfAbilities() {
    companion object {
        private var listOfAbilities: SingletonListOfAbilities? = null

        fun getInstance(context: Context): SingletonListOfAbilities =
            listOfAbilities ?: synchronized(this) {
                listOfAbilities ?: readFile(context).also { listOfAbilities = it}
            }

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