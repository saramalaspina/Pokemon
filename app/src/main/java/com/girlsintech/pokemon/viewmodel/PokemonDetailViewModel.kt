package com.girlsintech.pokemon.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.girlsintech.pokemon.connection.APIRequest
import com.girlsintech.pokemon.data.remote.ability.AbilityDescription
import com.girlsintech.pokemon.data.remote.evolution.Evolution
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.data.remote.species.Species
import com.google.gson.GsonBuilder
import org.json.JSONObject

//classe responsabile per la gestione dei dati ottenuti tramite le PokéAPI
class PokemonDetailViewModel(private var application: Application) : AndroidViewModel(application) {

    val pokemonInfo = MutableLiveData<PokemonInfo>()

    //metodo per ricavare le informazioni dall'url https://pokeapi.co/api/v2/pokemon/{id or name}/
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

    // metodo per ricavare le informazioni dall'url https://pokeapi.co/api/v2/pokemon-species/{id or name}/
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

    // metodo per ricavare le informazioni dall'url https://pokeapi.co/api/v2/evolution-chain/{id}/
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

    // metodo per ricavare le informazioni dall'url https://pokeapi.co/api/v2/ability/{id or name}/
    fun getAbility(url: String, onError: (String) -> Unit, onSuccess: (AbilityDescription) -> Unit){
        val queue = APIRequest.getAPI(application)
        queue.getPokemonInfo({
            val l = unpackAbility(it)
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

    // parser per mappare il contenuto del file json nella classe AbilityDescription
    private fun unpackAbility(it: JSONObject?): AbilityDescription {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, AbilityDescription::class.java)
    }

    // parser per mappare il contenuto del file json nella classe Evolution
    private fun unpackEvolution(it: JSONObject?): Evolution {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, Evolution::class.java)
    }

    // parser per mappare il contenuto del file json nella classe Species
    private fun unpackSpecies(it: JSONObject?): Species {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, Species::class.java)
    }

    // parser per mappare il contenuto del file json nella classe PokemonInfo
    private fun unpackPokemon(it: JSONObject?): PokemonInfo {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, PokemonInfo::class.java)
    }

    // classe Factory incaricata di creare l'istanza del ViewModel
    @Suppress("UNCHECKED_CAST")
    class PokemonDetailViewModelFactory(
        private val application: Application
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return PokemonDetailViewModel(application) as T
        }
    }
}