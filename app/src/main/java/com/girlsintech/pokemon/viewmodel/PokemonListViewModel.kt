package com.girlsintech.pokemon.viewmodel

import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.support.v7.graphics.Palette
import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.girlsintech.pokemon.connection.APIRequest
import com.girlsintech.pokemon.data.models.PokemonItem
import com.girlsintech.pokemon.data.models.PokemonList
import com.google.gson.GsonBuilder
import org.json.JSONObject


class PokemonListViewModel(private var app: Application) : AndroidViewModel(app) {

    val pokemonItemList = MutableLiveData<MutableList<PokemonItem>>()

    fun getData(onError: (String) -> Unit) {
        val queue = APIRequest.getAPI(app)
        queue.getPokemonList({
            val l = unpackProduct(it)
            pokemonItemList.postValue(l)
        }, {
            Log.w("XXX", "VolleyError")
            if (it?.message != null)
                onError(it.message!!)
            else
                onError("Network Error")
        },0,0)
    }


    private fun unpackProduct(it: JSONObject?): MutableList<PokemonItem> {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        val ret = gson.fromJson(json, PokemonList::class.java)
        return ret.pokemonList
    }

    fun calcDominantColor(drawable: Drawable, onFinish: (Color) -> Unit) {
        val bmp = (drawable as BitmapDrawable).bitmap.copy(Bitmap.Config.ARGB_8888, true)

        Palette.from(bmp).generate { palette ->
            palette?.dominantSwatch?.rgb?.let { colorValue ->
                onFinish(Color(colorValue))
            }
        }
    }
}

@Suppress("UNCHECKED_CAST")
class ProductViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonListViewModel(application) as T
    }
}

