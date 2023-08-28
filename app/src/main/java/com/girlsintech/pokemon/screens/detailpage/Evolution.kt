package com.girlsintech.pokemon.screens.detailpage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.girlsintech.pokemon.R
import com.girlsintech.pokemon.data.remote.evolution.Evolution
import com.girlsintech.pokemon.screens.fontBasic
import com.girlsintech.pokemon.ui.theme.CardBackground
import com.girlsintech.pokemon.util.TextInfo
import com.girlsintech.pokemon.viewmodel.PokemonViewModel

//component che mostra la lista delle evoluzioni di un Pokémon
@Composable
fun PokemonEvolutionSection(
    viewModelDb: PokemonViewModel,
    evolution: Evolution,
    bottomPadding: Dp
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize(1f)
            .padding(start = 25.dp, end = 25.dp, bottom = bottomPadding)
            //la colonna deve essere scrollabile verticalmente nella orientazione landscape
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val evolutionFirst = evolution.chain.species.name

        //controllo che esistano delle evoluzioni per il Pokémon
        if(evolution.chain.evolves_to.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_evolution),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 30.dp),
                textAlign = TextAlign.Center,
                fontFamily = fontBasic(),
                fontStyle = FontStyle.Italic,
                fontSize = 15.sp,
                color = Color.DarkGray
            )
        }

        //scorro tutte le evoluzioni del Pokémon mostrato nella pagina di dettaglio
        evolution.chain.evolves_to.forEach { evolves_to ->

            val evolutionSecond = evolves_to.species.name

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EvolutionBox(viewModelDb = viewModelDb, text = evolutionFirst)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.TwoTone.ArrowForward,
                        contentDescription = null,
                        tint = Color.DarkGray,
                        modifier = Modifier
                            .requiredSize(30.dp)
                    )
                    TextInfo(text = evolves_to.evolution_details[0].trigger.name)
                }
                EvolutionBox(viewModelDb = viewModelDb, text = evolutionSecond)
            }
            //per ogni evoluzione, scorro le successive
            evolves_to.evolves_to.forEach {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    EvolutionBox(viewModelDb = viewModelDb, text = evolutionSecond)
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.TwoTone.ArrowForward,
                            contentDescription = null,
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .requiredSize(30.dp)
                        )
                        TextInfo(text = it.evolution_details[0].trigger.name)
                    }
                    EvolutionBox(viewModelDb = viewModelDb, text = it.species.name)
                }
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
    }
}

//content che mostra il nome della specie nell'evoluzione e la sua immagine
@Composable
fun EvolutionBox(
    viewModelDb: PokemonViewModel,
    text: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .background(CardBackground, RoundedCornerShape(100))
                .requiredSize(115.dp)
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(
                        //ottengo l'url dell'immagine dal nome della specie evoluta, mantenuta nel database
                        viewModelDb.getImageFromName(text).observeAsState().value
                    )
                    .build(),
                contentDescription = null,
                modifier = Modifier
                    .requiredSize(95.dp)
            )
        }

        TextInfo(text = text, Color.Black)
    }
}

