package com.girlsintech.pokemon.util


sealed class Route(val route: String) {
    object Homepage: Route("pokemon_homepage")
    object ListPage: Route("pokemon_list_screen")
    object DetailPage: Route("pokemon_detail_screen")

    fun withArgs(dominantColor: Int, pokemonUrl: String) : String {
        return buildString {
            append(route)
            append("/$dominantColor")
            append("/$pokemonUrl")
        }
    }
}