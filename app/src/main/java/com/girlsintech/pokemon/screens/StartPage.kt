package com.girlsintech.pokemon.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.R
import androidx.compose.foundation.Image
import androidx.compose.ui.layout.ContentScale

@Composable
fun MainView(
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.sfondo),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds
        )

        Column {
            Spacer(modifier = Modifier.height(10.dp))
            myImage(R.drawable.pokemon_title)
            Spacer(modifier = Modifier.height(40.dp))
            myImage(R.drawable.pokemon)
            Spacer(modifier = Modifier.height(80.dp))
            Start(onClick)
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
                fontFamily = fontFamily()
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
                fontFamily = fontFamily()
            )
        }
    }

}



