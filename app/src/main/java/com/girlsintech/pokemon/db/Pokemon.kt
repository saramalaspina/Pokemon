package com.girlsintech.pokemon.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

//classe che corrisponde alla tabella Pokémon nel database
@Parcelize
@Entity
data class Pokemon(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var url: String,
    var img: String,
    var favorite: Int,
    var type: String,
    var generation: Int,
    var ability: String
) : Parcelable

//è una classe parcelizzabile in quanto è necessario mantenerne lo stato tra le ricomposizioni