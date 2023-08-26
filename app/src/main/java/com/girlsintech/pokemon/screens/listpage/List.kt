package com.girlsintech.pokemon.screens.listpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.viewmodel.PokemonViewModel


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PokemonList(
    list: List<Pokemon>,
    refresh: Boolean,
    viewModel: PokemonViewModel,
    navController: NavController,
    onRefresh: (Boolean) -> Unit
) {

    Column(
        Modifier
            .padding(start = 15.dp, end = 15.dp, bottom = 3.dp)
            .background(CardBackground, RoundedCornerShape(10.dp))
    ) {
        LazyColumn(
            Modifier
                .padding(top = 5.dp)
        ) {
            itemsIndexed(list) { _, pokemon ->
                ListItem(
                    text = {
                        PokemonItem(
                            pokemon = pokemon,
                            refresh = refresh,
                            viewModel = viewModel,
                            navController = navController,
                            onRefresh = onRefresh
                        )
                    }
                )
            }
        }
    }
}
