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

        //creo se necessario l'oggetto
        fun getInstance(context: Context): SingletonListOfAbilities =
            listOfAbilities ?: synchronized(this) {
                listOfAbilities ?: readFile(context).also { listOfAbilities = it}
            }

        //legge il file nel folder assets
        private fun readFile(context: Context): SingletonListOfAbilities {
            val listOfAbilities = SingletonListOfAbilities()
            //la cartella assets è una speciale cartella gestita dal SO, non presente fisicamente sul dispositivo mobile ma sul cabinet apk veicolato
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