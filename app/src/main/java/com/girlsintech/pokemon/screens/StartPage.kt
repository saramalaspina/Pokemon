package com.girlsintech.pokemon.screens

import android.content.res.Configuration
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.ui.theme.BluePokemon
import com.girlsintech.pokemon.ui.theme.Yellow

@Composable
fun MainView(
    onClickDiscover: () -> Unit,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxSize(),
        color = MaterialTheme.colors.background
    ) {

        val configuration = LocalConfiguration.current

        when (configuration.orientation) {
            Configuration.ORIENTATION_PORTRAIT -> {
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

                ConstraintLayout {
                    val (images, button, icon) = createRefs()

                    Column (modifier = Modifier
                        .constrainAs(images){
                            top.linkTo(parent.top, 10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }){
                        MyImage(R.drawable.pokemon_title)
                        Spacer(modifier = Modifier.height(40.dp))
                        MyImage(R.drawable.pokemon)
                    }

                    Column (modifier = Modifier
                        .constrainAs(button) {
                            top.linkTo(images.bottom, 50.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                    )  {
                        Start(onClickDiscover, onClick)
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 25.dp, bottom = 25.dp)
                            .constrainAs(icon) {
                                bottom.linkTo(parent.bottom)
                                end.linkTo(parent.end)
                            },
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
            else -> {
                Image(
                    modifier = Modifier
                        .fillMaxSize(),
                    painter = painterResource(id = R.drawable.sfondoh),
                    contentDescription = "background",
                    contentScale = ContentScale.FillBounds
                )

                ConstraintLayout {
                    val (images, button, icon) = createRefs()

                    Column (modifier = Modifier
                        .constrainAs(images){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 80.dp)
                            bottom.linkTo(parent.bottom)
                        }
                    ){
                        Image(painter = painterResource(id = R.drawable.pokemon_title),
                            contentDescription = "image"
                        )
                        Spacer(modifier = Modifier.height(0.dp))
                        Image(painter = painterResource(id = R.drawable.pokemon),
                            contentDescription = "image"
                        )
                    }

                    Column (modifier = Modifier
                        .constrainAs(button) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start, 165.dp)
                            bottom.linkTo(parent.bottom)
                        }
                    )  {
                        Start(onClickDiscover, onClick)
                    }

                    Column (modifier = Modifier
                        .constrainAs(icon) {
                            end.linkTo(parent.end, 30.dp)
                            bottom.linkTo(parent.bottom, 20.dp)
                        }
                    )  {
                        Image(
                            painter = painterResource(id = R.drawable.pokeball),
                            modifier = Modifier.size(60.dp),
                            contentDescription = null
                        )
                    }

                }
            }
        }
    }
}

@Composable
fun Start(
    onClickDiscover: () -> Unit,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,  //non ci sono spazi a sinistra e a destra ma solo tra gli oggetti
            verticalAlignment = Alignment.CenterVertically  //gli oggetti sono inseriti tutti nella parte alta dell'interfaccia
        ) {
            Text(
                text = stringResource(id = R.string.start),
                modifier = Modifier
                    .padding(4.dp)
                    .weight(1f)
                    .padding(8.dp),
                color = BluePokemon,
                fontSize = 30.sp,
                textAlign = TextAlign.Center,
                fontFamily = fontPokemon()
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

       Column {

           Button(
               modifier = Modifier
                   .wrapContentWidth()
                   .width(250.dp),
               onClick = onClick,
               shape = RoundedCornerShape(30),
               colors = ButtonDefaults.buttonColors(BluePokemon)
           ) {
               Row {
                   Text(
                       text = "Pokedex",
                       modifier = Modifier
                           .padding(4.dp)
                           .weight(1f)
                           .padding(8.dp),
                       color = Color.White,
                       fontSize = 25.sp,
                       textAlign = TextAlign.Center,
                       fontFamily = fontPokemon()
                   )

                   Image(
                       painter = painterResource(id = R.drawable.grey_pokeball),
                       contentDescription = "pokeball",
                       modifier = Modifier
                           .requiredSize(45.dp)
                           .align(Alignment.CenterVertically)
                   )
               }
           }

           Spacer(modifier = Modifier.height(20.dp))

           Button(
               modifier = Modifier
                   .wrapContentWidth()
                   .width(250.dp),
               onClick = onClickDiscover,
               shape = RoundedCornerShape(30),
               colors = ButtonDefaults.buttonColors(Yellow)
           ) {
               Row {
                   Text(
                       text = stringResource(id = R.string.discover),
                       modifier = Modifier
                           .padding(4.dp)
                           .weight(1f)
                           .padding(8.dp),
                       color = Color.White,
                       fontSize = 25.sp,
                       textAlign = TextAlign.Center,
                       fontFamily = fontPokemon()
                   )

                   Image(
                       painter = painterResource(id = R.drawable.grey_pokeball),
                       contentDescription = "pokeball",
                       modifier = Modifier
                           .requiredSize(45.dp)
                           .align(Alignment.CenterVertically)
                   )
               }
           }
       }
    }
}



