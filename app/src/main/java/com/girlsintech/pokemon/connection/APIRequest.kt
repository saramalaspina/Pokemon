package com.girlsintech.pokemon.connection

import android.content.Context
import androidx.compose.runtime.MutableState
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.girlsintech.pokemon.data.remote.species.Species
import org.json.JSONObject

//API per l'utilizzo della classe Volley
class APIRequest (context: Context){
    //RequestQueue viene usata per mettere in coda le richieste e gestire le risposte ed è responsabile della gestione dei thread di lavoro
    private var request: RequestQueue

    companion object {
        private var apiRequest: APIRequest? = null

        fun getAPI(context: Context): APIRequest {
            if (apiRequest == null) {
                apiRequest = APIRequest(context)

            }
            return apiRequest!!
        }
    }

    init {
        request = Volley.newRequestQueue(context)
    }

    fun getPokemonInfo(onSuccess: (JSONObject?) -> Unit,
                       onFail: (VolleyError?) -> Unit, url: String){

        val jsonLoader = JsonObjectRequest(url,
            onSuccess,
            onFail)
        jsonLoader.retryPolicy = DefaultRetryPolicy(5000,
            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
        request.add(jsonLoader)
    }

}