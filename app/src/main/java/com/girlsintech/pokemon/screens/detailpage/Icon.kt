package com.girlsintech.pokemon.screens.detailpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Favorite
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.girlsintech.pokemon.db.Pokemon
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.viewmodel.PokemonViewModel

@Composable
fun TopIcons(
    pokemon: Pokemon,
    viewModel: PokemonViewModel,
    navController: NavController
) {
    var fav by remember {
        mutableStateOf(pokemon.favorite)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        ConstraintLayout (modifier = Modifier.fillMaxWidth()) {
            val (arrow, favorite) = createRefs()

            Icon(
                Icons.TwoTone.ArrowBack,
                contentDescription = null,
                tint = BluePokemon,
                modifier = Modifier
                    .constrainAs(arrow) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start, 15.dp)
                        bottom.linkTo(parent.bottom)
                    }
                    .requiredSize(33.dp)
                    .clickable {
                        navController.popBackStack()
                    }
            )
            Icon(
                Icons.TwoTone.Favorite,
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(favorite) {
                        top.linkTo(parent.top, 2.dp)
                        end.linkTo(parent.end, 25.dp)
                        bottom.linkTo(parent.bottom)
                    }
                    .requiredSize(33.dp)
                    .clickable {
                        pokemon.favorite = 1 - pokemon.favorite
                        fav = 1 - fav
                        viewModel.update(pokemon)
                    }
                    .size(30.dp),
                tint = if (fav == 1) Color.Red else Color.White
            )
        }
    }
}