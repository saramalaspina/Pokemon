package com.girlsintech.pokemon.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.Bottom
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.ui.theme.BluePokemon

@Composable
fun MainView(
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Row {
            Icon(
                Icons.TwoTone.ArrowBack,
                contentDescription = null,
                tint = Color.White,
                modifier = Modifier
                    .size(50.dp)
                    .padding(top = 15.dp, start = 15.dp)

            )
            Image(
                modifier = Modifier.fillMaxSize(),
                painter = painterResource(id = R.drawable.sfondo),
                contentDescription = "background",
                contentScale = ContentScale.FillBounds
            )
        }

        Column {
            Spacer(modifier = Modifier.height(10.dp))
            MyImage(R.drawable.pokemon_title)
            Spacer(modifier = Modifier.height(40.dp))
            MyImage(R.drawable.pokemon)
            Spacer(modifier = Modifier.height(80.dp))
            Start(onClick)
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 25.dp, bottom = 25.dp),
            horizontalAlignment = Alignment.End,
            verticalArrangement = Bottom
        ) {
            Image(
                painter = painterResource(id = R.drawable.pokeball),
                modifier = Modifier.size(60.dp),
                contentDescription = null
            )
        }
    }
}

@Composable
fun Start(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Center
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth(), //questa riga deve occupare tutto lo schermo
            horizontalArrangement = Arrangement.SpaceBetween,  //non ci sono spazi a sinistra e a destra ma solo tra gli oggetti
            verticalAlignment = Alignment.CenterVertically  //gli oggetti sono inseriti tutti nella parte alta dell'interfaccia
        ) {
            Text(
                text = "Let's Start!",
                modifier = Modifier
                    .padding(4.dp)
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp),
                color = BluePokemon,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontFamily = fontPokemon()
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Button(
            modifier = Modifier
                .wrapContentWidth()
                .padding(end = 90.dp)
                .padding(start = 90.dp),
            onClick = onClick,
            shape = RoundedCornerShape(30),
            colors = ButtonDefaults.buttonColors(BluePokemon)
        ) {
            Row {
                Text(
                    text = "Pokedex",
                    modifier = Modifier
                        .padding(4.dp)
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 25.sp,
                    textAlign = TextAlign.Center,
                    fontFamily = fontPokemon()
                )

                Image(painter = painterResource(id = R.drawable.grey_pokeball),
                    contentDescription = "pokeball",
                    modifier = Modifier
                        .requiredSize(45.dp)
                        .align(Alignment.CenterVertically)
                )
            }
        }
    }
}



