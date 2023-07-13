package com.girlsintech.pokemon.viewmodel

import android.app.Application
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.*
import com.girlsintech.pokemon.connection.APIRequest
import com.girlsintech.pokemon.data.models.PokemonItem
import com.girlsintech.pokemon.data.remote.responses.PokemonList
import com.girlsintech.pokemon.util.Constants.PAGE_SIZE
import com.girlsintech.pokemon.util.Resource
import com.google.gson.GsonBuilder
import org.json.JSONObject
import java.util.*


class PokemonListViewModel(private var app: Application) : AndroidViewModel(app) {

    var pokemonItemList = mutableStateOf<List<PokemonItem>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)

    private var curPage = 0

    init {
        getData()
    }

    fun getData() {
        var result : PokemonList
        isLoading.value = true

        val queue = APIRequest.getAPI(app)
        queue.getPokemonList({
            result = unpackProduct(it)
            loadPokemonList(Resource.Success(result))
        }, {
            Log.w("XXX", "VolleyError")
            if (it?.message != null)
                loadError.value = it.message!!
            else
                loadError.value = "Network Error"
        },
            PAGE_SIZE,
            curPage * PAGE_SIZE
        )
    }

    private fun loadPokemonList(result : Resource<PokemonList>){
        endReached.value = curPage * PAGE_SIZE >= result.data!!.count
        val pokedexEntries = result.data.results.mapIndexed { index, entry ->
            val number = if(entry.url.endsWith("/")) {
                entry.url.dropLast(1).takeLastWhile { it.isDigit() }
            } else {
                entry.url.takeLastWhile { it.isDigit() }
            }
            val url = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/${number}.png"
            PokemonItem(entry.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }, url, number.toInt(), false,null)
        }
        curPage++

        loadError.value = ""
        isLoading.value = false
        pokemonItemList.value += pokedexEntries
    }

    private fun unpackProduct(it: JSONObject?): PokemonList {
        val json = it?.toString()
        val gson = GsonBuilder().create()
        return gson.fromJson(json, PokemonList::class.java)
    }

}


@Suppress("UNCHECKED_CAST")
class PokemonViewModelFactory(
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PokemonListViewModel(application) as T
    }
}

