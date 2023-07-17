package com.girlsintech.pokemon.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pokemon(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var name: String,
    var url: String,
    var img: String,
    var favorite: Int
)