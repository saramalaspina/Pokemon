package com.girlsintech.pokemon.db

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

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