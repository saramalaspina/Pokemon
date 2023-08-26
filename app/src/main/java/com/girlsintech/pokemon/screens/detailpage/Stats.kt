package com.girlsintech.pokemon.screens.detailpage

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.girlsintech.pokemon.data.remote.responses.PokemonInfo
import com.girlsintech.pokemon.screens.fontBasic
import com.girlsintech.pokemon.util.parseStatToAbbr
import com.girlsintech.pokemon.util.parseStatToColor

//component che contiene una data statistica di cui viene mostrato il valore e il nome
@Composable
fun PokemonDetailStats(
    statName: String,
    statValue: Int,
    statMaxValue: Int,
    statColor: Color,
    animDuration: Int = 1000,
    animDelay: Int = 0
) {
    var animationPlayed by remember {
        mutableStateOf(false)
    }

    //il valore e la barra della statistica vengono incrementati tramite un'animazione
    val curPercent = animateFloatAsState(
        targetValue = if (animationPlayed) {
            statValue / statMaxValue.toFloat()
        } else 0f,
        animationSpec = tween(
            animDuration,
            animDelay
        )
    )

    LaunchedEffect(key1 = true) {
        animationPlayed = true
    }

    Row(modifier = Modifier.padding(start = 30.dp)){
        Text(
            text = statName,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            fontFamily = fontBasic()
        )
    }

    Box(
        modifier = Modifier
            .padding(start = 25.dp, end = 45.dp)
            .fillMaxWidth()
            .height(25.dp)
            .clip(CircleShape)
            .background(Color.LightGray)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(25.dp)
                .fillMaxWidth(curPercent.value)
                .clip(CircleShape)
                .background(statColor)
                .padding(horizontal = 10.dp)
        ) {
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (num) = createRefs()
                Text(
                    text = (curPercent.value * statMaxValue).toInt().toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp,
                    fontFamily = fontBasic(),
                    modifier = Modifier.constrainAs(num){
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(parent.end, 5.dp)
                    }
                )
            }
        }
    }
}

//component che mostra tutte le statische del Pokémon
@Composable
fun PokemonStatSection(
    pokemonInfo: PokemonInfo
) {
    val scrollState = rememberScrollState()
    Column (
        modifier = Modifier
            .fillMaxSize(1f)
            //la colonna deve essere scrollabile verticalmente nella orientazione landscape
            .verticalScroll(scrollState)
    ){
        pokemonInfo.stats.forEach { stat ->
            PokemonDetailStats(
                statName = parseStatToAbbr(stat),
                statValue = stat.base_stat,
                statMaxValue = pokemonInfo.stats.maxOf { it.base_stat },
                statColor = parseStatToColor(stat),
                animDelay = 500
            )
            Spacer(modifier = Modifier.height(6.dp))
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}