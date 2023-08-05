package com.girlsintech.pokemon.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.girlsintech.pokemon.connection.APIRequest
import com.girlsintech.pokemon.data.remote.evolution.Evolution
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.data.remote.species.Species
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.util.Resource
import com.google.gson.GsonBuilder
import org.json.JSONObject

class PokemonDetailViewModel(private var application: Application) : AndroidViewModel(application) {

    val pokemonInfo = MutableLiveData<PokemonInfo>()

    fun getData(url: String, onError: (String) -> Unit) {
        val queue = APIRequest.getAPI(application)
        queue.getPokemonInfo({
            val l = unpackPokemon(it)
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

    fun getSpecies(url: String, onError: (String) -> Unit, onSuccess: (Species) -> Unit){
        val queue = APIRequest.getAPI(application)
        queue.getPokemonInfo({
            val l = unpackSpecies(it)
            onSuccess(l)
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

    fun getEvolution(url: String, onError: (String) -> Unit, onSuccess: (Evolution) -> Unit){
        val queue = APIRequest.getAPI(application)
        queue.getPokemonInfo({
            val l = unpackEvolution(it)
            onSuccess(l)
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

    private fun unpackEvolution(it: JSONObject?): Evolution {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, Evolution::class.java)
    }

    private fun unpackSpecies(it: JSONObject?): Species {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, Species::class.java)
    }
    private fun unpackPokemon(it: JSONObject?): PokemonInfo {
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