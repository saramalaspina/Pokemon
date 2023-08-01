package com.girlsintech.pokemon.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.girlsintech.pokemon.connection.APIRequest
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.db.Pokemon
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject

class PokemonDetailViewModel(private var application: Application) : AndroidViewModel(application) {

    val pokemonInfo = MutableLiveData<PokemonInfo>()

    fun getData(url: String, onError: (String) -> Unit) {
        val queue = APIRequest.getAPI(application)
        queue.getPokemonInfo({
            val l = unpackProduct(it)
            pokemonInfo.postValue(l)
        }, {
            Log.w("XXX", "VolleyError")
            if (it?.message != null)
                onError(it.message!!)
            else
                onError("Network Error")
        },
            url
        )
    }

    private fun unpackProduct(it: JSONObject?): PokemonInfo {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, PokemonInfo::class.java)
    }

    @Suppress("UNCHECKED_CAST")
    class PokemonDetailViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PokemonDetailViewModel(application) as T
        }
    }
}