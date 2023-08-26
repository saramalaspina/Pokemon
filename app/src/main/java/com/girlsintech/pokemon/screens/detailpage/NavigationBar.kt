package com.girlsintech.pokemon.screens.detailpage

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.screens.fontBasic

//la barra di navigazione permette di navigare tra le diverse caratteristiche del pokemon
@Composable
fun NavigationBar(
    onClick: (Int) -> Unit
) {
    var selectedAbout by remember {
        mutableStateOf(true)
    }

    var selectedStats by remember {
        mutableStateOf(false)
    }

    var selectedEvolution by remember {
        mutableStateOf(false)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 25.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ){
        Column(
            modifier = Modifier
                .clickable {
                    selectedAbout = true
                    selectedStats = false
                    selectedEvolution = false
                    onClick(0) //mostra le informazioni generali
                }
        ) {
            Text(
                text = stringResource(id = R.string.about),
                fontFamily = fontBasic(),
                color = if (selectedAbout) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedAbout) FontWeight.Bold else FontWeight(10)
            )
        }
        Column(
            modifier = Modifier
                .clickable {
                    selectedStats = true
                    selectedAbout = false
                    selectedEvolution = false
                    onClick(1) //mostra le statistiche
                }
        ) {
            Text(
                text = stringResource(id = R.string.stats),
                fontFamily = fontBasic(),
                color = if (selectedStats) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedStats) FontWeight.Bold else FontWeight(10)
            )
        }
        Column(
            modifier = Modifier
                .clickable {
                    selectedEvolution = true
                    selectedAbout = false
                    selectedStats = false
                    onClick(2) //mostra le evoluzioni
                }
        ) {
            Text(
                text = stringResource(id = R.string.evolutions),
                fontFamily = fontBasic(),
                color = if (selectedEvolution) Color.Black else Color.Gray,
                fontSize = 20.sp,
                fontWeight = if (selectedEvolution) FontWeight.Bold else FontWeight(10)
            )
        }
    }
}